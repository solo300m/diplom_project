package com.example.MyProjectWithSecurity.controllers;

import com.example.MyProjectWithSecurity.Repositories.Book2UserRepository;
import com.example.MyProjectWithSecurity.Repositories.UserRepository;
import com.example.MyProjectWithSecurity.Service.AuthorService;
import com.example.MyProjectWithSecurity.data.*;
import com.example.MyProjectWithSecurity.Service.BookService;
import com.example.MyProjectWithSecurity.errs.EmptySearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchAuthorController {
    private final BookService bookService;
    private AuthorService authorService;
//    private Book2User book2User;
//    private UserRepository userRepository;
//    private Book2UserRepository book2UserRepository;

    @Autowired
    public SearchAuthorController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
//        this.book2User = book2User;
//        this.userRepository = userRepository;
//        this.book2UserRepository = book2UserRepository;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto(){
        return new SearchWordDto();
    }
    @ModelAttribute("searchResults")
    public List<Book> searchResults(){
        return new ArrayList<>();
    }

    /*@GetMapping("/search")
    public String openSearchPage(){
        Logger.getLogger(SearchAuthorController.class.getName()).info("Open search page");
//        Map<String, List<Book>> list = bookService.getAuthorName(found);
//        model.addAttribute("books",list);
        return "/books/author.html";
    }

    @GetMapping("/search/{found}")
    public String searchPage(@PathVariable("found") String found, Model model){
        Logger.getLogger(SearchAuthorController.class.getName()).info("Open searchAuthor page");
        Map<String, List<Book>> list = authorService.getAuthorName(found);
        model.addAttribute("books",list);
        return "/books/author.html";
    }*/
    @GetMapping(value = {"/search","/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                   Model model) throws EmptySearchException {
        if(searchWordDto != null){
        model.addAttribute("searchWordDto",searchWordDto);
        model.addAttribute("searchResults",
                bookService.getPageOfSearchResultBooks(searchWordDto.getExample(),0,5).getContent());
        return "/search/index";
        }
        else{
            throw new EmptySearchException("Поиск  по null не возможен");
        }
    }
    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto){
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(),offset,limit).getContent());
    }
}
