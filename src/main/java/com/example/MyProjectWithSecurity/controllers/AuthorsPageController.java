package com.example.MyProjectWithSecurity.controllers;

import com.example.MyProjectWithSecurity.Repositories.Book2UserRepository;
import com.example.MyProjectWithSecurity.Repositories.UserRepository;
import com.example.MyProjectWithSecurity.Service.AuthorService;
import com.example.MyProjectWithSecurity.Service.BookService;
import com.example.MyProjectWithSecurity.data.Authors;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book2User;
import com.example.MyProjectWithSecurity.data.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller

//@RequestMapping("/authors")
//@Api(description = "authors data")
public class AuthorsPageController {

    private BookService bookService;
    private AuthorService authorService;
    private UserRepository userRepository;
    private Book2UserRepository book2UserRepository;

    @Autowired
    public AuthorsPageController(BookService bookService, AuthorService authorService, UserRepository userRepository, Book2UserRepository book2UserRepository) {
        this.bookService = bookService;
        this.authorService = authorService;
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


    @GetMapping("/authors")
    public String reloadAuthors(Model model){
        model.addAttribute("bookList",bookService.getBookData());
        model.addAttribute("authors",authorService.getMapAuthors(authorService.getAuthorsList()));
        return "/authors/index";
    }

    @GetMapping("/authors/genres")
    public String genresPage(){
        Logger.getLogger(MainPageController.class.getName()).info("Opened page genres");
        return "/genres/index.html";
    }

    @GetMapping("/authors/bookshop")
    public String mainOpen(Model model){
        Logger.getLogger(GenresPageController.class.getName()).info("Back on the main-page");
        model.addAttribute("bookData", bookService.getBookData());
        return "index.html";
    }
    /*@GetMapping("/authors")
    public String authorPage(){
        Logger.getLogger(MainPageController.class.getName()).info("Opened page authors");
        return "/authors/index.html";
    }*/
    @GetMapping("/authors/{id}")
    public String slugPage(@PathVariable("id") int id, Model model){
        //Logger.getLogger(MainPageController.class.getName()).info("Opened page slug");
        model.addAttribute("authors",authorService.getAuthorId(id).getAuthor());
        model.addAttribute("bio",authorService.getAuthorId(id).getBiography());
        model.addAttribute("photo",authorService.getAuthorId(id).getPhoto());
        model.addAttribute("books",authorService.getIdAuthorBook(id));
        Logger.getLogger(MainPageController.class.getName()).info("Opened page slug"/*+model.getAttribute("bio")*/);
        return "/authors/slugs/slug.html";
    }

    @ApiOperation("method to get map of authors")
    @GetMapping("/api/authors")
    @ResponseBody
    public Map<String, List<Authors>> authors(){
        return authorService.getMapAuthors(authorService.getAuthorsList());
    }
}
