package com.example.MyProjectWithSecurity.controller;

import com.example.MyProjectWithSecurity.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
//@RequestMapping("/postponed")
public class PostponedController {

    private final BookService bookService;

    @Autowired
    public PostponedController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/postponed")
    public String postponedPage(){
        Logger.getLogger(PostponedController.class.getName()).info("Open posponed page");
        return "/postponed.html";
    }
}
