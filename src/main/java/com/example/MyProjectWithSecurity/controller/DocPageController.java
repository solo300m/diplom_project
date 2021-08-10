package com.example.MyProjectWithSecurity.controller;

import com.example.MyProjectWithSecurity.Repositories.Book2UserRepository;
import com.example.MyProjectWithSecurity.Repositories.UserRepository;
import com.example.MyProjectWithSecurity.Service.AnalitikService;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book2User;
import com.example.MyProjectWithSecurity.data.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class DocPageController {

    private final UserRepository userRepository;
    private final Book2UserRepository book2UserRepository;
    private final AnalitikService analitikService;

    public DocPageController(UserRepository userRepository, Book2UserRepository book2UserRepository,
                             AnalitikService analitikService) {
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
        this.analitikService = analitikService;
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

    @GetMapping("/docs")
    public String docsPage(){
        return "/Documents/Documents.html";
    }

    @GetMapping("/company")
    public String companyPage(){
        return "/Documents/about_kompany.html";
    }

    @GetMapping("/help")
    public String helpPage(){
        return "/Documents/help.html";
    }

    @GetMapping("/customers")
    public String customersPage(){
        return "/Documents/customer.html";
    }
}
