package com.example.MyProjectWithSecurity.controller;

import com.example.MyProjectWithSecurity.Repositories.Book2UserRepository;
import com.example.MyProjectWithSecurity.Repositories.UserRepository;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.Service.BookService;

import com.example.MyProjectWithSecurity.data.Book2User;
import com.example.MyProjectWithSecurity.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/recent")
public class RecentController {

    private final BookService bookService;
    private final UserRepository userRepository;
    private final Book2UserRepository book2UserRepository;

    @Autowired
    public RecentController(BookService bookService, UserRepository userRepository, Book2UserRepository book2UserRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
    }

    @ModelAttribute("postponedCount")
    public Integer postponedCound(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user);
            List<Book2User>filteredList = list.stream()
                    .filter(c->c.getBook_file_type().getCode().equals("KEPT"))
                    .collect(Collectors.toList());
            List<Book> booksPost = new ArrayList<>();
            for(Book2User book : filteredList)
                booksPost.add(book.getBook());
            return booksPost.size();
        }
        else{
            return 0;
        }
    }
    @ModelAttribute("catCount")
    public Integer catCound(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user);
            List<Book2User>filteredList = list.stream()
                    .filter(c->c.getBook_file_type().getId() == 2)
                    .collect(Collectors.toList());
            List<Book> booksPost = new ArrayList<>();
            for(Book2User book : filteredList)
                booksPost.add(book.getBook());
            return booksPost.size();
        }
        else{
            return 0;
        }
    }

    @ModelAttribute("userCustom")
    public String userCustom(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);

            return user.getUser_name();
        }
        else{
            return "не определен";
        }
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks(){
        //bookService.setAuthorsData(bookService.getBookData());
        //bookService.updateBookIdAuthors();
        return bookService.getListOfNewBooks();
    }
    @ModelAttribute("searchResults")
    public List<Book> searchResults(){
        return new ArrayList<>();
    }

    @GetMapping("/recent")
    public String recentPage(Model model){
        model.addAttribute("searchResults",bookService.getPageOfNewBooks(0,5).getContent());
        Logger.getLogger(RecentController.class.getName()).info("Opened page recent");
        return "/recent.html";
    }
    @GetMapping("/recent/bookshop")
    public String mainPageReturn(){
        Logger.getLogger(RecentController.class.getName()).info("Reload great page!");
        return "index";
    }
}
