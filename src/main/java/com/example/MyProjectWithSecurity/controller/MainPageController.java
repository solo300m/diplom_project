package com.example.MyProjectWithSecurity.controller;

import com.example.MyProjectWithSecurity.Repositories.*;
import com.example.MyProjectWithSecurity.Service.AnalitikService;
import com.example.MyProjectWithSecurity.Service.AuthorService;
import com.example.MyProjectWithSecurity.data.*;
import com.example.MyProjectWithSecurity.Service.BookService;
import com.example.MyProjectWithSecurity.Service.HibernateService;
import com.example.MyProjectWithSecurity.security.jwt.JWTUtil;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/bookshop")
public class MainPageController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final HibernateService hibernateService;
    private Integer teg_old = 0;
    private final UserRepository userRepository;
    private final Book2UserRepository book2UserRepository;
    private final AnalitikService analitikService;
    private final JWTUtil jwtUtil;


    @Autowired
    public MainPageController(BookService bookService, AuthorService authorService, HibernateService hibernateService,
                              UserRepository userRepository, Book2UserRepository book2UserRepository,
                              AnalitikService analitikService, JWTUtil jwtUtil) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.hibernateService = hibernateService;
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
        this.analitikService = analitikService;
        this.jwtUtil = jwtUtil;
    }


    /**
     * Возвращает количество книг в "Отложено"
     * Используется в header страницы для индикации текущего состояния сервиса "Отложено"
     * @return - количество отложенных книг
     */
    @ModelAttribute("postponedCount")
    public Integer postponedCound(){
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();*/
        User user = new User();
        user = analitikService.autorizationGetUser();
        if(user != null) {
            /*User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user);
            List<Book2User>filteredList = list.stream()
                    .filter(c->c.getBook_file_type().getCode().equals("KEPT"))
                    .collect(Collectors.toList());*/
            List<Book2User>filteredList = analitikService.getUserBooksStatus(user,"KEPT");
            /*List<Book> booksPost = new ArrayList<>();
            for(Book2User book : filteredList)
                booksPost.add(book.getBook());*/
            List<Book>booksPost = analitikService.getBooksFromBook2User(filteredList);
            return booksPost.size();
        }
        else{
            return 0;
        }
    }

    /**
     * Возвращает количество книг в корзине
     * Используется в header страницы для индикации текущего состояния сервиса "Корзина"
     * @return - количество книг в корзине
     */
    @ModelAttribute("catCount")
    public Integer catCound(){
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();*/
        User user = new User();
        user = analitikService.autorizationGetUser();
        if(user != null) {
            /*User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user);
            List<Book2User>filteredList = list.stream()
                    .filter(c->c.getBook_file_type().getId() == 2)
                    .collect(Collectors.toList());*/
            List<Book2User>filteredList = analitikService.getUserBooksStatus(user,"CART");
            List<Book> booksPost = analitikService.getBooksFromBook2User(filteredList);
            /*List<Book> booksPost = new ArrayList<>();
            for(Book2User book : filteredList)
                booksPost.add(book.getBook());*/
            return booksPost.size();
        }
        else{
            return 0;
        }
    }

    /*@ModelAttribute("cookieArray")
    public List<Cookie> getCookieArray(HttpServletRequest request){

        List<Cookie> cookieArr = Arrays.stream(request.getCookies()).collect(Collectors.toList());

            Cookie cookieCurent = null;
            for (Cookie cookie : cookieArr) {
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Активные cookie " + cookie.getName() + " " + cookie.getValue());
                if (cookie.getName().equals("token")) {
                    cookieCurent = cookie;
                }

            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                BookstoreUserDetails userDetails = (BookstoreUserDetails) authentication.getPrincipal();
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Token Name - " + userDetails.getUsername()
                        + " Name user is " + userDetails.getBookstoreUser().getName() + " validate is "
                        + userDetails.isAccountNonExpired());

                Claims claims = Jwts.parser()
                        .setSigningKey("skillbox")
                        .parseClaimsJws(cookieCurent.getValue())
                        .getBody();
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Идентификатор пользователя " + claims.getSubject());
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Время входа в систему " + new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(claims.getIssuedAt()));
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Срок действия " + new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(claims.getExpiration()));
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Роль пользователя " + claims.get("role"));
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Параметр getAudience() " + claims.getAudience());
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Параметр getIssuer() " + claims.getIssuer());
                Logger.getLogger(MainPageController.class.getSimpleName()).info("Параметр getNotBefore() " + claims.getNotBefore());

            }


        return cookieArr;
    }*/

    /**
     * Выводит имя текущего пользователя или сообщение "не определен"
     * @return - имя пользователя, авторизированного в системе либо строку "не определен"
     */
    @ModelAttribute("userCustom")
    public String userCustom(){
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();*/
        User user = analitikService.autorizationGetUser();
        if(user != null) {
            /*User user = new User();
            user = userRepository.findUserByEmailContains(username);*/
            return user.getUser_name();
        }
        else{
            return "не определен";
        }
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults(){
        return new ArrayList<>();
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks(){
        //bookService.setAuthorsData(bookService.getBookData());
        //bookService.updateBookIdAuthors();
        return bookService.getPageOfRecommendedBooks(0,6).getContent();
    }

    @ModelAttribute("newBooks")
    public List<Book> newBooks(){
        return bookService.getPageOfNewBooks(0,6).getContent();
    }

    @ModelAttribute("bestseller")
    public List<Book> bestsellerBook(){
        return bookService.getPageOfBestseller(0,6).getContent();
    }

    @ModelAttribute("tegs")
    public List<Tag> tegsBook(){

        return bookService.getTegs();
    }

    @GetMapping("/")
    public String reloadMainPage(Model model){
        Map<Tag,List<Book>> tagListMap = bookService.getMapTegs(bookService.getBookData());
        model.addAttribute("tagmap",tagListMap);
        model.addAttribute("avrg",bookService.averigMatTegListSize(tagListMap));
        //model.addAttribute("recommendedBooks", bookService.getPageOfRecommendedBooks(0,6).getContent());
        Logger.getLogger(MainPageController.class.getName()).info("Reload great page!");
        return "index";
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit){
      return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset,limit).getContent());
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto getNewBooksPage(@RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit){
        return new BooksPageDto(bookService.getPageOfNewBooks(offset,limit).getContent());
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BooksPageDto getBestsellersPage(@RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit){
        return new BooksPageDto(bookService.getPageOfBestseller(offset, limit).getContent());
    }

    @GetMapping("/bookshop")
    public String mainPage(Model model){
        model.addAttribute("book2author",hibernateService.getBookData2());
        Map<Tag,List<Book>> tagListMap = bookService.getMapTegs(bookService.getBookData());
        model.addAttribute("tagmap",tagListMap);
        model.addAttribute("avrg",bookService.averigMatTegListSize(tagListMap));
        //model.addAttribute("recommendedBooks", bookService.getPageOfRecommendedBooks(0,6).getContent());
        Logger.getLogger(MainPageController.class.getName()).info("Hy, it is great page!");
        return "index";
    }
    @GetMapping("/bookshop/genres")
    public String genresPage(Model model){
        model.addAttribute("genre",bookService.getGenreAll());
        Logger.getLogger(MainPageController.class.getName()).info("Opened page genres");
        return "/genres/index.html";
    }
    @GetMapping("/bookshop/authors")
    public String authorPage(Model model){
        Logger.getLogger(MainPageController.class.getName()).info("Opened page authors from main_page");
        model.addAttribute("authors",authorService.getMapAuthors(authorService.getAuthorsList()));
        return "/authors/index.html";
    }
    @GetMapping("/bookshop/recent")
    public String recentPage(){
        Logger.getLogger(MainPageController.class.getName()).info("Opened page recent from main_page");
        return "/recent.html";
    }

    @GetMapping("/bookshop/{teg}")
    public String tegPage(@PathVariable("teg") Integer teg, Model model){
        teg_old = teg;
        model.addAttribute("searchResults", bookService.getPageOfSearchBooksTeg(teg_old,0,5).getContent());
        model.addAttribute("tagName", bookService.getTegName(teg));
        model.addAttribute("count",bookService.getBookOfTeg(teg_old).size());
        Logger.getLogger(MainPageController.class.getName()).info("Open teg page");
        return "/tags/index.html";
    }

    @GetMapping("/books/tag/{teg}")
    @ResponseBody
    public BooksPageDto getNextTagsPage(@RequestParam("offset")Integer offset,//@PathVariable(value = "teg" ,required = false) Integer teg
                                        @RequestParam("limit") Integer limit){
        //teg = teg_old;
        //model.addAttribute("searchResults", bookService.getPageOfSearchBooksTeg(teg_old,0,5).getContent());
        return new BooksPageDto(bookService.getPageOfSearchBooksTeg(teg_old,offset,limit).getContent());
    }

}
