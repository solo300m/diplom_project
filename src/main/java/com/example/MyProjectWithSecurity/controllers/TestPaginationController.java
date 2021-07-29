package com.example.MyProjectWithSecurity.controllers;

import com.example.MyProjectWithSecurity.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestPaginationController {
    private BookService bookService;
    private Integer offset_glob;

    @Autowired
    public TestPaginationController(BookService bookService) {
        this.bookService = bookService;
        offset_glob = 0;
    }
    @GetMapping("/test")
    public String getTestPageMain(){
        return"/test_page/test_index.html";
    }
    /*@GetMapping("/test/pagination")
    public String getOpenPagination(){
        return"/test_page/index_pagination.html";
    }*/
    @GetMapping("/test/books/by-author")
    public String getBookByAuthor(@RequestParam("author") String authorName, Model model){
        model.addAttribute("books_author",bookService.getBooksByAuthor(authorName));
        return "/test_page/test_books_by_author.html";
    }
    @GetMapping("/test/pagination")
    public String getPageTest(@RequestParam("offset") Integer offset, Model model){
        offset_glob = offset;
        model.addAttribute("offset",offset);
        model.addAttribute("books",bookService.getPageOfRecommendedBooks(offset,6).getContent());
        return "/test_page/index_pagination.html";
    }
    @GetMapping("/test/pagination/right")
    public String getPageRightOneStep(Model model){
        if(bookService.getPageOfRecommendedBooks(offset_glob,6).getContent().size()==6)
            ++offset_glob;
        model.addAttribute("offset",offset_glob);
        model.addAttribute("books",bookService.getPageOfRecommendedBooks(offset_glob,6).getContent());
        return "/test_page/index_pagination.html";
    }
    @GetMapping("/test/pagination/left")
    public String getPageLeftOneStep(Model model){
        if(offset_glob!=0)
            --offset_glob;
        model.addAttribute("offset",offset_glob);
        model.addAttribute("books",bookService.getPageOfRecommendedBooks(offset_glob,6).getContent());
        return "/test_page/index_pagination.html";
    }
}
