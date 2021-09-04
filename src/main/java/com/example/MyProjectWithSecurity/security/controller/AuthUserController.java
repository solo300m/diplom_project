package com.example.MyProjectWithSecurity.security.controller;

import com.example.MyProjectWithSecurity.security.data.BookstoreUserRegister;
import com.example.MyProjectWithSecurity.security.data.ContactConfirmationPayload;
import com.example.MyProjectWithSecurity.security.data.ContactConfirmationResponse;
import com.example.MyProjectWithSecurity.security.data.RegistrationForm;
import com.example.MyProjectWithSecurity.security.service.JWTBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister userRegister;
    private final JWTBlackListService jwtBlackListService;
//    private final UserRepository userRepository;
//    private final Book2UserRepository book2UserRepository;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister, JWTBlackListService jwtBlackListService) {
        this.userRegister = userRegister;
//        this.userRepository = userRepository;
//        this.book2UserRepository = book2UserRepository;
        this.jwtBlackListService = jwtBlackListService;
    }


    @GetMapping("/signin")
    public String handleSignin(HttpServletRequest request){
        jwtBlackListService.addBlackList(request); //добавлено для записи токена в BlackList
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
        Cookie jwtCopy = new Cookie("tokenCopy",loginResponse.getResult()); //создание копии для сохранения в BlackList токенов

        httpServletResponse.addCookie(cookie);
        httpServletResponse.addCookie(jwtCopy);
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

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = MyJWTException.class)
//    @ResponseBody
//    public String handlerError(MyJWTException myJWTException){
//        Logger.getLogger(AuthUserController.class.getSimpleName()).info("ExceptionHandler called");
//        return "/signin";
//    }
}
