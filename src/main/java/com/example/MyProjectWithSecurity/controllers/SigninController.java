//package com.example.MyBookWithSecurity.controllers;
//
//import com.example.MyBookWithSecurity.Service.BookService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.logging.Logger;
//@Controller
//public class SigninController {
//
//    private final BookService bookService;
//
//    @Autowired
//    public SigninController(BookService bookService) {
//        this.bookService = bookService;
//    }
//
//    @GetMapping("/signin")
//    public String popularPage(){
//        Logger.getLogger(SigninController.class.getName()).info("Open signin page");
//        return "signin";
//    }
//
//}
