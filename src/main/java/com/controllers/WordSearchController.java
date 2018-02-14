package com.controllers;

import com.database.Database;
import com.models.User;
import com.models.Word;
import com.work.assignments.FileIO.MultiThreadedWordSearchService;
import com.work.assignments.FileIO.Query;
import com.work.assignments.FileIO.Result;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping(value = "/")
public class WordSearchController {
    @GetMapping(value = "/login")
    public ModelAndView getLogin() {
        return new ModelAndView("login","user",new User());
    }

    @PostMapping(value = "/login")
    public String authenticate(@ModelAttribute("user") User user, BindingResult result) {
        if(result.hasErrors()){
            return "error";
        }
        if(Database.checkUserExists(user.getUserName(), user.getPassword())) {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession();
            session.setAttribute("userName", user.getUserName());
            System.out.println("session created" + session.getAttribute("userName"));
            return "redirect:wordSearch";
        }
        else {
            return "redirect:login";
        }

    }

    @GetMapping(value = "/wordSearch")
    public ModelAndView wordSearch() {
        return new ModelAndView("wordSearch","word", new Word());
    }

    @PostMapping(value = "/results")
    public @ResponseBody String searchResults(@ModelAttribute("word") Word word, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "error";
        }
        if(!word.getWord().equals("") && !word.getDirectory().equals("")) {
            System.out.println("WordSearch Hit: Get Word: " + word.getWord() + "Get Directory: " + word.getDirectory());
            MultiThreadedWordSearchService wordSearchService = new MultiThreadedWordSearchService();
            List<Result> resultList = wordSearchService.search(new Query(word.getDirectory(), word.getWord(), true));
            JSONArray resultArray = new JSONArray();
            for (Result result : resultList) {
                JSONObject obj = new JSONObject();
                obj.put("lineNumber",result.getLineNumber());
                obj.put("positionNumber", result.getPositionNumber());
                obj.put("fileName",result.getFileName());
                resultArray.add(obj);
            }
            return resultArray.toJSONString();
        }
        return null;
    }

    @GetMapping(value = "/logout")
    public String logout() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        session.invalidate();
        return "redirect:login";
    }

}
