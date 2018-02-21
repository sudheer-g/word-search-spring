package com.controllers;

import com.models.Word;
import com.work.assignments.FileIO.MultiThreadedWordSearchService;
import com.work.assignments.FileIO.Query;
import com.work.assignments.FileIO.Result;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@RequestMapping(value = "/")
public class WordSearchController {

    @GetMapping(value = "/wordSearch")
    public ModelAndView wordSearch() {
        return new ModelAndView("wordSearch", "word", new Word());
    }

    @PostMapping(value = "/results", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<Result> searchResults(@ModelAttribute("word") Word word, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return null;
        }
        if (!word.getWord().equals("") && !word.getDirectory().equals("")) {
            System.out.println("WordSearch Hit: Get Word: " + word.getWord() + "Get Directory: " + word.getDirectory());
            MultiThreadedWordSearchService wordSearchService = new MultiThreadedWordSearchService();
            return wordSearchService.search(new Query(word.getDirectory(), word.getWord(), true));
        }
        return null;
    }
}
