package com.example.MyProjectWithSecurity.controllers;

import com.example.MyProjectWithSecurity.Service.BookService;
import com.example.MyProjectWithSecurity.data.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class PopularController {

    private final BookService bookService;

    @Autowired
    public PopularController(BookService bookService) {
        this.bookService = bookService;
    }
    @ModelAttribute("searchResults")
    public List<Book> searchResults(){
        return new ArrayList<>();
    }

    @GetMapping("/popular")
    public String popularPage(Model model){
        model.addAttribute("searchResults",bookService.getPageOfBestseller(0,5).getContent());
        Logger.getLogger(PopularController.class.getName()).info("Open popular page");
        return "/popular.html";
    }
}
