package com.example.MyProjectWithSecurity.controller;


import com.example.MyProjectWithSecurity.Repositories.*;
//import com.example.MyProjectWithSecurity.Repositories.Book2UserTypeRepository;
//import com.example.MyProjectWithSecurity.Repositories.BookRepository;
//import com.example.MyProjectWithSecurity.Repositories.UserRepository;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book2User;
import com.example.MyProjectWithSecurity.data.Book2User_type;
import com.example.MyProjectWithSecurity.data.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BookshopCartController {


    private UserRepository userRepository;
    private Book2UserRepository book2UserRepository;
    private Book2UserTypeRepository book2UserTypeRepository;

    public BookshopCartController(UserRepository userRepository, Book2UserRepository book2UserRepository, Book2UserTypeRepository book2UserTypeRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
        this.book2UserTypeRepository = book2UserTypeRepository;
        this.bookRepository = bookRepository;
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


    @ModelAttribute(name="bookCart")
    public List<Book> bookCart(){
        return new ArrayList<>();
    }

    private final BookRepository bookRepository;

    //private EntityManagerFactory emf=Persistence.createEntityManagerFactory("factory");




//    @GetMapping("/cart")
//    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents, Model model){
//        if(cartContents == null || cartContents.equals("")){
//            model .addAttribute("isCartEmpty", true);
//
//        }else{
//            model.addAttribute("isCartEmpty", false);
//            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
//            cartContents = cartContents.endsWith("/") ? cartContents.substring(0,cartContents.length()-1): cartContents;
//            String[] cookieSlug = cartContents.split("/");
//            List<Book> booksFromCookieSlug = bookRepository.findBooksBySlugIn(cookieSlug);
//            model.addAttribute("bookCart", booksFromCookieSlug);
//
//        }
//        return "cart";
//    }
    @GetMapping("/cart")
    public String handleCartRequest(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user)
                    .stream().filter(c->c.getBook_file_type().getId() == 2)
                    .collect(Collectors.toList());
            List<Book> booksPost = new ArrayList<>();
            for(Book2User book : list)
                booksPost.add(book.getBook());
            model.addAttribute("bookCart", booksPost);
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Open BookCartController page");
            return "/cart";

        }
        else{
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Open Signin page");
            return "/signin";
        }
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCarRequest(@PathVariable("slug") String slug ,@CookieValue(name="cartContents",
            required = false) String cartContents, HttpServletResponse response, Model model){

        if(cartContents != null || !cartContents.equals("")){
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);

        }else {
            model.addAttribute("isCartEmpty", true);
        }
        return "redirect:/books/cart";
    }

//    @PostMapping("/changeBookStatus/{slug}")
//    public String handleChangeBookStatus(@PathVariable("slug") String slug, @CookieValue(name="cartContents",
//            required = false) String cartContents, HttpServletResponse response, Model model){
//
//        if(cartContents == null || cartContents.equals("")){
//            Cookie cookie = new Cookie("cartContents", slug);
//            cookie.setPath("/books");
//            response.addCookie(cookie);
//            model.addAttribute("isCartEmpty", false);
//        }
//        else if(!cartContents.contains(slug)){
//            StringJoiner stringJoiner = new StringJoiner("/");
//            stringJoiner.add(cartContents).add(slug);
//            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
//            cookie.setPath("/books");
//            response.addCookie(cookie);
//            model.addAttribute("isCartEmpty",false);
//        }
//        return "redirect:/books/"+slug;
//    }

    @GetMapping("/cart/{sl}")
    public String handleCartAdd(@PathVariable("sl") String sl, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")){
            Book book = bookRepository.findBookBySlug(sl);
            Logger.getLogger(BookPostponedController.class.getName()).info("SlugBookPostponed Page open  "+sl);
            Book2User book2User = new Book2User();
            book2User.setBook(book);
            book2User.setUser(userRepository.findUserByEmailContains(username));
            book2User.setBook_file_type(book2UserTypeRepository.findBook2User_typeByCode("CART"));
            book2User.setTime(LocalDateTime.now());
            book2UserRepository.save(book2User);
            model.addAttribute("slugBook", book);
            List<Book2User> listNull = new ArrayList<>();
            listNull = book2UserRepository.findBook2UsersByBookIs(bookRepository.findBookById(null));
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Null records in BD are "+listNull.size());
            if(listNull.size()!=0)
                book2UserRepository.deleteAll(listNull);

            return "/books/slug";
        }else{
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current is anonymousUser");
            return "/signin";
        }
    }
    @GetMapping("/cart/postponed/{sl}")
    public String handleCartToPostponed(@PathVariable("sl") String sl, Model model){
        Book book = bookRepository.findBookBySlug(sl);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = new User();
        user = userRepository.findUserByEmailContains(username);
        List<Book2User>allListUser = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User>filtListUser = allListUser.stream().filter(c->c.getBook().getId() == book.getId() && c.getBook_file_type().getId() == 2).collect(Collectors.toList());
        if(filtListUser.size() >= 1){
            Book2User book2User = new Book2User();
            book2User = filtListUser.get(0);
            Book2User_type book2User_type = new Book2User_type();
            book2User_type.setId(1);
            book2User.setBook_file_type(book2User_type);
            book2UserRepository.save(book2User);
        }

        List<Book2User> book2UserList = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User> filteredList = book2UserList.stream().filter(c->c.getBook_file_type().getId()==2).collect(Collectors.toList());

        List<Book> bookList = new ArrayList<>();
        for(Book2User btu : filteredList)
            bookList.add(btu.getBook());
        model.addAttribute("bookCart", bookList);
        return "/cart";
    }

    @GetMapping("/cart/del/{sl}")
    public String handleCartDel(@PathVariable("sl") String sl, Model model){
        Book book = bookRepository.findBookBySlug(sl);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = new User();
        user = userRepository.findUserByEmailContains(username);
        List<Book2User>allListUser = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User>filtListUser = allListUser.stream().filter(c->c.getBook().getId() == book.getId() && c.getBook_file_type().getId() == 2).collect(Collectors.toList());
        if(filtListUser.size() >= 1){
            Book2User book2User = new Book2User();
            book2User = filtListUser.get(0);

            book2UserRepository.delete(book2User);
        }

        List<Book2User> book2UserList = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User> filteredList = book2UserList.stream().filter(c->c.getBook_file_type().getId()==2).collect(Collectors.toList());

        List<Book> bookList = new ArrayList<>();
        for(Book2User btu : filteredList)
            bookList.add(btu.getBook());
        model.addAttribute("bookCart", bookList);
        return "/cart";
    }
}
