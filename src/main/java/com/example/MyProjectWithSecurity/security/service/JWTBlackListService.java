package com.example.MyProjectWithSecurity.security.service;

import com.example.MyProjectWithSecurity.security.repository.JWTBlackListRepository;
import com.example.MyProjectWithSecurity.data.JWTBlackList;
import com.example.MyProjectWithSecurity.security.data.BookstoreUserRegister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class JWTBlackListService {
    public final JWTBlackListRepository jwtBlackListRepository;
    public final BookstoreUserRegister userRegister;


    public JWTBlackListService(JWTBlackListRepository jwtBlackListRepository, BookstoreUserRegister userRegister) {
        this.jwtBlackListRepository = jwtBlackListRepository;
        this.userRegister = userRegister;
    }

    /**
     * Метод возвращает из базы данных список невалидных токенов
     * @return список невалидных токенов
     */
    public List<JWTBlackList> initBlackList(){
        List<JWTBlackList> jwtBlackLists = jwtBlackListRepository.findAll();
        if(jwtBlackLists.size() == 0 || jwtBlackLists == null){
            return new ArrayList<>();
        }
        else
            return jwtBlackLists;
    }

    /**
     * Метод добавляет токен в список и базу данных BlackList токенов
     * метод запускается при открытии страницы signin.html
     * @param request - HTTPServletRequest - получение списка действующих токенов
     * @return 0 - операция добавления прошла успешно, -1 - произошла ошибка
     */
    public Integer addBlackList(HttpServletRequest request){
        List<JWTBlackList>jwtBlackLists = initBlackList();
        Cookie[]cookies = request.getCookies();
        String tokenContext = "";
        String tokenName = "";
        if(cookies != null){
            for(Cookie coo : cookies){
                if(coo.getName().equals("tokenCopy")){
                    tokenContext = coo.getValue();
                    tokenName = coo.getName();
                }
            }
            boolean chek = true;
            for(JWTBlackList item : jwtBlackLists){
                if(item.getTokenValue().equals(tokenContext)) {
                    chek = false;
                    break;
                }
                else{
                    chek = true;
                }
            }
            if(chek == true){
                JWTBlackList jwtSave = new JWTBlackList();
                jwtSave.setTokenValue(tokenContext);
                jwtBlackListRepository.save(jwtSave);
                jwtBlackLists.add(jwtSave);
                return 0;
            }
            else{
                Logger.getLogger(JWTBlackListService.class.getSimpleName()).info("JWT-token"+tokenName+"in BlackList allready");
                return -1;
            }
        }else{
            Logger.getLogger(JWTBlackListService.class.getSimpleName()).info("JWT-token with name Token do not founded");
            return 1;
        }
    }

    /**
     * метод tokenTerminator - неудачная тестовая попытка уничтожить jwt-токен
     * в работе программы не используется, нужен был для проверки версии
     * @param request
     * @param response
     */
    public void tokenTerminator(HttpServletRequest request, HttpServletResponse response){
        Cookie[]cookies = request.getCookies();
        String[]tokenJWT = null;
        String payload = "";
        if(cookies != null){
            for(Cookie coo : cookies){
                if(coo.getName().equals("token")){

                    Cookie cookie = new Cookie("token","");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                    SecurityContextHolder.clearContext();
                }
            }
        }
    }
}
