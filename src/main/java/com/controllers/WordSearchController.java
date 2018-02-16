package com.controllers;

import com.models.Word;
import com.work.assignments.FileIO.MultiThreadedWordSearchService;
import com.work.assignments.FileIO.Query;
import com.work.assignments.FileIO.Result;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping(value = "/")
public class WordSearchController {

    @GetMapping(value = "/wordSearch")
    public ModelAndView wordSearch() {
        return new ModelAndView("wordSearch", "word", new Word());
    }

    @PostMapping(value = "/results")
    public @ResponseBody
    String searchResults(@ModelAttribute("word") Word word, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "error";
        }
        if (!word.getWord().equals("") && !word.getDirectory().equals("")) {
            System.out.println("WordSearch Hit: Get Word: " + word.getWord() + "Get Directory: " + word.getDirectory());
            MultiThreadedWordSearchService wordSearchService = new MultiThreadedWordSearchService();
            List<Result> resultList = wordSearchService.search(new Query(word.getDirectory(), word.getWord(), true));
            JSONArray resultArray = new JSONArray();
            for (Result result : resultList) {
                JSONObject obj = new JSONObject();
                obj.put("lineNumber", result.getLineNumber());
                obj.put("positionNumber", result.getPositionNumber());
                obj.put("fileName", result.getFileName());
                resultArray.add(obj);
            }
            return resultArray.toJSONString();
        }
        return null;
    }


}
