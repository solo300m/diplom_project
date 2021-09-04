package com.example.MyProjectWithSecurity.security.controller;

import com.example.MyProjectWithSecurity.errs.MyJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class JWTExceptionController extends ResponseEntityExceptionHandler {

//    @GetMapping("/signin")
//    public String getErrorJWT(){
//        return "/signin.html";
//    }

    @ResponseStatus(value = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ExceptionHandler(value = MyJWTException.class)
    @ResponseBody
    public String handlerError(MyJWTException myJWTException){
        return "/signin";
    }
}
