package com.example.MyProjectWithSecurity.controllers;

import com.example.MyProjectWithSecurity.Repositories.*;

import com.example.MyProjectWithSecurity.Service.AuthorService;
import com.example.MyProjectWithSecurity.Service.BookService;
import com.example.MyProjectWithSecurity.Service.GenreService;
import com.example.MyProjectWithSecurity.data.*;
import com.example.MyProjectWithSecurity.data.Book2Genre;
import com.example.MyProjectWithSecurity.data.SearchIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/genres")
public class GenresPageController {
    private BookService bookService;
    private AuthorService authorService;
    private GenreService genreService;
    private Integer genre_old = 0;
    private Integer offset = 0;
    private Integer limit = 5;
    private boolean flag = true;
    private SearchIdDto searchIdDto = new SearchIdDto();
    private List<Book2Genre>listBook = new ArrayList<>();
    private UserRepository userRepository;
    private Book2UserRepository book2UserRepository;

    @Autowired
    public GenresPageController(BookService bookService, AuthorService authorService, GenreService genreService, UserRepository userRepository, Book2UserRepository book2UserRepository) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
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

    @ModelAttribute("searchIdDto")
    public SearchIdDto searchIdDto(){
        return new SearchIdDto();
    }
    @ModelAttribute("listBook")
    public List<Book2Genre>listBook(){
        return new ArrayList<>();
    }

    @GetMapping("/genres")
    public String rederectGenres(Model model){
        model.addAttribute("genre",bookService.getGenreAll());
        model.addAttribute("b2g",genreService.getMapBook2Genre());
        Logger.getLogger(GenresPageController.class.getName()).info("Reload genres page");
        return "/genres/index";
    }
    @GetMapping("/genres/bookshop")
    public String mainOpen(Model model){
        Logger.getLogger(GenresPageController.class.getName()).info("Back on the main-page");
        model.addAttribute("bookData", bookService.getBookData());
        return "index.html";
    }
    @GetMapping("/genres/authors")
    public String authorPage(Model model){
        Logger.getLogger(MainPageController.class.getName()).info("Opened page authors from Genres_Page");
        model.addAttribute("authors",authorService.getMapAuthors(authorService.getAuthorsList()));
        return "/authors/index.html";
    }

    @GetMapping("/books/{genre}")
    public String genreSlugPage(@PathVariable(value = "genre",required = false) Integer genre, Model model){
        genre_old = genre;
        listBook.clear();
        offset = 0;
        Logger.getLogger(GenresPageController.class.getSimpleName()).info("This is genre controller");
        model.addAttribute("gen",genreService.getGenreOfId(genre));
        //model.addAttribute("listBook",genreService.getListBookOfGenre(genreService.getMapBook2Genre(), genre));
        //model.addAttribute("listBook",genreService.getPageBookOfGenre(genre_old,0,5));
//        List<Book2Genre>book2GenreList = genreService.getPageBookOfGenre(genre_old,0,5).getContent();
//        List<Book>bookList = new ArrayList<>();
//        for(Book2Genre b2:book2GenreList){
//            bookList.add(b2.getBook());
//        }
        searchIdDto.setIdParam(genre);
        listBook.addAll(genreService.getPageBookOfGenre(genre_old,offset,limit).getContent());
        model.addAttribute("searchIdDto",searchIdDto);
        model.addAttribute("listBook",listBook);
        model.addAttribute("count",genreService.getListBookOfGenre(genreService.getMapBook2Genre(), genre).size());
        if((genreService.getPageBookOfGenre(genre_old,offset,limit).getContent().size())==limit){
            ++offset;
            if((genreService.getPageBookOfGenre(genre_old,offset,limit).getContent().size())==0)
                --offset;
        }
        return "/genres/slug.html";
    }
    @GetMapping("/next/page/")
    public String getNextPageIdGenre(Model model){
        Logger.getLogger(GenresPageController.class.getSimpleName()).info("The Lost controller");
        if(flag) {
            listBook.addAll(genreService.getPageBookOfGenre(genre_old, offset, limit).getContent());
            flag = false;
        }
        model.addAttribute("gen",genreService.getGenreOfId(genre_old));
        model.addAttribute("listBook", listBook);
        model.addAttribute("searchIdDto",searchIdDto);
        model.addAttribute("count",genreService.getListBookOfGenre(genreService.getMapBook2Genre(), genre_old).size());
        if((genreService.getPageBookOfGenre(genre_old,offset,limit).getContent().size())==limit &&
                (genreService.getPageBookOfGenre(genre_old,offset+1,limit).getContent().size())!=0){
             ++offset;
            flag = true;
        }
        return "/genres/slug";
    }

    /*@GetMapping("/books/gonre/{genre}")
    @ResponseBody
    public Book2GenrePageDto getNextGenrePage(@RequestParam("offset") Integer offset,
                                         @RequestParam("limit") Integer limit,
                                         @PathVariable(value = "genre",required = false) Integer genre){
//        List<Book2Genre>book2GenreList = genreService.getPageBookOfGenre(genre.getIdParam(),offset,limit).getContent();
//        List<Book>bookList = new ArrayList<>();
//        for(Book2Genre b2:book2GenreList){
//            bookList.add(b2.getBook());
//        }
        return new Book2GenrePageDto(genreService.getPageBookOfGenre(searchIdDto.getIdParam(),offset,limit).getContent());
    }*/
}
