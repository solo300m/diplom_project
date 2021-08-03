package com.example.MyProjectWithSecurity.controller;

import com.example.MyProjectWithSecurity.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class CartController {

    private final BookService bookService;

    @Autowired
    public CartController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/cart")
    public String popularPage(){
        Logger.getLogger(CartController.class.getName()).info("Open cart page");
        return "/cart.html";
    }
}

