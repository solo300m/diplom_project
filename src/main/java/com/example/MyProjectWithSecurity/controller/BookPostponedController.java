package com.example.MyProjectWithSecurity.controller;

import com.example.MyProjectWithSecurity.Repositories.*;

import com.example.MyProjectWithSecurity.Service.AnalitikService;
import com.example.MyProjectWithSecurity.Service.BookService;
import com.example.MyProjectWithSecurity.Service.HibernateService;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book2User;
import com.example.MyProjectWithSecurity.data.Book2User_type;
import com.example.MyProjectWithSecurity.data.User;
import com.example.MyProjectWithSecurity.security.data.BookstoreUser;
import com.example.MyProjectWithSecurity.security.repository.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/postponed")
public class BookPostponedController {
    private final Book2UserRepository book2UserRepository;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;
    private final UserRepository userRepository;
    private final HibernateService hibernateService;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final AnalitikService analitikService;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BookPostponedController(Book2UserRepository book2UserRepository,
                                   BookstoreUserRepository bookstoreUserRepository,
                                   Book2UserTypeRepository book2UserTypeRepository,
                                   HibernateService hibernateService, UserRepository userRepository, BookService bookService, BookRepository bookRepository, AnalitikService analitikService) {
        this.book2UserRepository = book2UserRepository;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.book2UserTypeRepository = book2UserTypeRepository;
        this.userRepository = userRepository;
        this.hibernateService = hibernateService;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
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

    /**
     * Контроллер обрабатывающий нажатие на кнопку Отложено. Сохраняет запись в БД со статусом status
     * @param sl - slug поле объекта Book
     * @param model - model аттребуты для установки параметров книги (заголовок, автор и т.д)
     * @return - URL страницы перехода
     */
    @GetMapping("/postponed/{sl}")
    public String handlePostponed(@PathVariable("sl") String sl, Model model){
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();*/
        User user = analitikService.autorizationGetUser();
        if(user != null){
            Book book = bookRepository.findBookBySlug(sl);
            analitikService.book2UserSaveBookWithStatus(book,user,"KEPT");
            model.addAttribute("slugBook", book);
            Logger.getLogger(BookPostponedController.class.getName()).info("SlugBookPostponed Page open  "+sl);
            /*Book2User book2User = new Book2User();
            book2User.setBook(book);
            book2User.setUser(user);
            book2User.setBook_file_type(book2UserTypeRepository.findBook2User_typeByCode("KEPT"));
            book2User.setTime(LocalDateTime.now());
            book2UserRepository.save(book2User);

            List<Book2User> listNull = new ArrayList<>();
            listNull = book2UserRepository.findBook2UsersByBookIs(bookRepository.findBookById(null));
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Null records in BD are "+listNull.size());
            if(listNull.size()!=0)
                book2UserRepository.deleteAll(listNull);*/

            /*BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(username);
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current email is "+username);
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current name is "+bookstoreUser.toString());
            //book2UserRepository.deleteByBookIs(bookRepository.findBookById(null));*/
            return "/books/slug";
        }
        else{
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current is anonymousUser");
            return"signin";
        }
    }

    /**
     * Контроллер открывающий страницу Отложено /postponed.html
     * @param model - model загрузка списка отложенных книг
     * @return - URL страницы перехода
     */
    @GetMapping("/postponed/box")
    public String handlePostponedBox(Model model){
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();*/
        User user = analitikService.autorizationGetUser();
        if(user != null) {
            /*User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user)
                    .stream().filter(c->c.getBook_file_type().getCode().equals("KEPT"))
                    .collect(Collectors.toList());*/
            List<Book2User> list = analitikService.getUserBooksStatus(user,"KEPT");
            List<Book> booksPost = analitikService.getBooksFromBook2User(list);
            /*List<Book> booksPost = new ArrayList<>();
            for(Book2User book : list)
                booksPost.add(book.getBook());*/
            model.addAttribute("postponedBooks", booksPost);
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Open BookPostponedController page");
            return "/postponed";

        }
        else{
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Open Signin page");
            return "/signin";
        }
    }

    @GetMapping("/postponed/cart/{sl}")
    public String handlePostponedToCart(@PathVariable("sl") String sl, Model model){
        Book book = bookRepository.findBookBySlug(sl);
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = new User();
        user = userRepository.findUserByEmailContains(username);*/
        User user = analitikService.autorizationGetUser();
        List<Book2User>allListUser = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User>filtListUser = allListUser.stream().filter(c->c.getBook().getId() == book.getId() && c.getBook_file_type().getId() == 1).collect(Collectors.toList());
        if(filtListUser.size() >= 1){
            Book2User book2User = new Book2User();
            book2User = filtListUser.get(0);
            Book2User_type book2User_type = new Book2User_type();
            book2User_type.setId(2);
            book2User.setBook_file_type(book2User_type);
            book2UserRepository.save(book2User);
        }

        List<Book2User> book2UserList = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User> filteredList = book2UserList.stream().filter(c->c.getBook_file_type().getId()==1).collect(Collectors.toList());

        List<Book> bookList = new ArrayList<>();
        for(Book2User btu : filteredList)
            bookList.add(btu.getBook());
        model.addAttribute("postponedBooks", bookList);
        return "/postponed";
    }
    @GetMapping("/postponed/del/{sl}")
    public String handlePostponedDel(@PathVariable("sl") String sl, Model model){
        Book book = bookRepository.findBookBySlug(sl);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = new User();
        user = userRepository.findUserByEmailContains(username);
        List<Book2User>allListUser = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User>filtListUser = allListUser.stream().filter(c->c.getBook().getId() == book.getId() && c.getBook_file_type().getId() == 1).collect(Collectors.toList());
        if(filtListUser.size() >= 1){
            Book2User book2User = new Book2User();
            book2User = filtListUser.get(0);
//            Book2User_type book2User_type = new Book2User_type();
//            book2User_type.setId(2);
//            book2User.setBook_file_type(book2User_type);
            book2UserRepository.delete(book2User);
        }

        List<Book2User> book2UserList = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User> filteredList = book2UserList.stream().filter(c->c.getBook_file_type().getId()==1).collect(Collectors.toList());

        List<Book> bookList = new ArrayList<>();
        for(Book2User btu : filteredList)
            bookList.add(btu.getBook());
        model.addAttribute("postponedBooks", bookList);
        return "/postponed";
    }
}
