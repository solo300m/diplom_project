package com.example.MyProjectWithSecurity.security.controller;

import com.example.MyProjectWithSecurity.security.jwt.JWTUtil;
import com.example.MyProjectWithSecurity.security.service.JWTBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/test_page")
public class TestJwtTokenController {
    private final JWTUtil jwtUtil;
    private final JWTBlackListService jwtBlackListService;


    @Autowired
    public TestJwtTokenController(JWTUtil jwtUtil, JWTBlackListService jwtBlackListService) {
        this.jwtUtil = jwtUtil;
        this.jwtBlackListService = jwtBlackListService;
    }

    /**
     * Контроллер getTokenPage разработан для тестирования функциональности jwt-токенов
     * используется для открытия тестовой страницы ../test_page/token_test.html
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/token_test")
   public String getTokenPage(HttpServletRequest request, Model model){
       Cookie[]cookies = request.getCookies();
       String[]token = null;
       if(cookies != null){
           model.addAttribute("cookie",cookies);
           for(Cookie coo : cookies){
               if(coo.getName().equals("token")){
                   String foo = coo.getValue();
                    token = foo.split("\\.");
               }
           }

           if (token != null && token.length != 0) {
               model.addAttribute("token", token);
               Base64.Decoder decoder = Base64.getDecoder();
               String header = new String(decoder.decode(token[0]));
               String payload = new String(decoder.decode(token[1]));
               //String tail = new String(decoder.decode(token[2]));
               String[] decodArr = {header, payload};
               model.addAttribute("decod", decodArr);

               char[] ch = payload.toCharArray();
               StringBuffer buff = new StringBuffer();
               for (char c : ch) {
                   if (c != '"' && (c != '{' && c != '}')) {
                       buff.append(c);
                   }
               }
               payload = buff.toString();
               String[] payloadArr = payload.split("\\,");
               Map<String, String> map = new TreeMap<>();
               for (String s : payloadArr) {
                   String[] test = s.trim().split("\\:");

                   map.put(test[0], test[1]);

               }
               model.addAttribute("mapr", map);

               Long experat = Long.parseLong(map.get("exp"));
               Long iat = Long.parseLong(map.get("iat"));
               LocalDateTime ex = LocalDateTime.ofInstant(Instant.ofEpochSecond(experat), ZoneId.systemDefault());
               LocalDateTime i = LocalDateTime.ofInstant(Instant.ofEpochSecond(iat), ZoneId.systemDefault());
               model.addAttribute("experati", ex);
               model.addAttribute("both", i);
           }
       }
       return "/test_page/token_test";
   }

    /**
     * Тестовый контроллер используется для проверки методов работы с BlackList тоненов
     * тестирование добавления в blacklist по нажатию кнопки формы ../test_page/token_test.html
     * @param request
     * @param response
     * @return
     */
   @PostMapping("/terminate_token")
    public String getTerminateToken(HttpServletRequest request, HttpServletResponse response){
        jwtBlackListService.tokenTerminator(request, response);
        jwtBlackListService.addBlackList(request);
        return "redirect:/test_page/token_test";
   }
}
