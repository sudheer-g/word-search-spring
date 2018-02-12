package com.controllers;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@SessionAttributes("user")
public class WordSearchController {
    @GetMapping(value = "/services/login")
    public ModelAndView getLogin() {
        return new ModelAndView("login","user",new User());
    }
    @PostMapping(value = "/services/login")
    public String authenticate(@Validated @ModelAttribute("user") User user, BindingResult result, ModelMap model) {
        if(result.hasErrors()){
            return "error";
        }
        System.out.println("hit: " + user.getUserName() + user.getPassword());
        if(user.getPassword().equals("123")) {
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

    @GetMapping(value = "services/wordSearch")
    public ModelAndView wordSearch() {
        return new ModelAndView("wordSearch","word", new Word());
    }

    @PostMapping(value = "services/results")
    public @ResponseBody String searchResults(@Validated @ModelAttribute("word") Word word, BindingResult bindingResult, ModelMap model) {
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
        return "redirect:wordSearch";
    }

    @GetMapping(value = "services/logout")
    public String logout() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        session.invalidate();
        return "redirect:login";
    }
}
