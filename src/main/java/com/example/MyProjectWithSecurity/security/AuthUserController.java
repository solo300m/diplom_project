package com.example.MyProjectWithSecurity.security;

import com.example.MyProjectWithSecurity.Repositories.Book2UserRepository;
import com.example.MyProjectWithSecurity.Repositories.UserRepository;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book2User;
import com.example.MyProjectWithSecurity.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister userRegister;
//    private final UserRepository userRepository;
//    private final Book2UserRepository book2UserRepository;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister) {
        this.userRegister = userRegister;
//        this.userRepository = userRepository;
//        this.book2UserRepository = book2UserRepository;
    }

//    @ModelAttribute("postponedCount")
//    public Integer postponedCound(){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if(!username.equals("anonymousUser")) {
//            User user = new User();
//            user = userRepository.findUserByEmailContains(username);
//            List<Book2User> list = book2UserRepository.findBook2UsersByUser(user);
//            List<Book2User>filteredList = list.stream().filter(c->c.getBook_file_type().equals("KEPT")).collect(Collectors.toList());
//            List<Book> booksPost = new ArrayList<>();
//            for(Book2User book : filteredList)
//                booksPost.add(book.getBook());
//            return booksPost.size();
//        }
//        else{
//            return 0;
//        }
//    }

    @GetMapping("/signin")
    public String handleSignin(){
        Logger.getLogger(AuthUserController.class.getSimpleName()).info("Open signin page from AuthUserController!");
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSingup(Model model){
        Logger.getLogger(AuthUserController.class.getSimpleName()).info("Open signUp page from AuthUserController!");
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload pay ){
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");//в варианте работы с сессиями значение Boolean
        return response;
    }


    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload){
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");//в варианте работы с сессиями значение Boolean
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model){
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk",true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse){
        //return userRegister.login(payload); //данная строка используется при использовании механизма сессий. Все что ниже для токенов.
        //____________Механизм использования токенов JWT___________________________//
        //----HttpServletResponse httpServletResponse в параментах тоже добавлен для схемы работы с токенами--//
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token",loginResponse.getResult());

        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(){
        return "my.html";
    }

    @GetMapping("/profile")
    public String hendleProfile(Model model){
        model.addAttribute("curUsr", userRegister.getCurrentUser());
        return "profile.html";
    }

//    @GetMapping("/logout") //Данный метод нужен только в схеме с сессиями. Работа с токенами организована на стороне SecurityConfig
//    public String handleLogout(HttpServletRequest request){
//        HttpSession session = request.getSession();
//        SecurityContextHolder.clearContext();
//        if(session != null){
//            session.invalidate();
//        }
//        for(Cookie cookie : request.getCookies()){
//            cookie.setMaxAge(0);
//        }
//        return "redirect:/";
//    }
}
