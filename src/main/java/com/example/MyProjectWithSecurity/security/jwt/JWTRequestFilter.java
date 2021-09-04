package com.example.MyProjectWithSecurity.security.jwt;

import com.example.MyProjectWithSecurity.data.JWTBlackList;
import com.example.MyProjectWithSecurity.security.repository.JWTBlackListRepository;
import com.example.MyProjectWithSecurity.errs.MyJWTException;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetailService;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailService bookstoreUserDetailService;
    private final JWTUtil jwtUtil;
    private final JWTBlackListRepository jwtBlackListRepository;
    //private final BookstoreUserDetails bookstoreUserDetails;

    @Autowired
    public JWTRequestFilter(BookstoreUserDetailService bookstoreUserDetailService, JWTUtil jwtUtil, JWTBlackListRepository jwtBlackListRepository) {
        this.bookstoreUserDetailService = bookstoreUserDetailService;
        this.jwtUtil = jwtUtil;

        //this.bookstoreUserDetails = bookstoreUserDetails;
        this.jwtBlackListRepository = jwtBlackListRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("token")){
                    token = cookie.getValue();
                    List<JWTBlackList>blackLists = jwtBlackListRepository.findAll();
                    for(JWTBlackList jl:blackLists){
                        if(token.equals(jl.getTokenValue())){
                            token="";
                        }
                    }
//
                    try {
                        if(token.equals("")){
                            username = null;
                        }
                        else
                            username = jwtUtil.extractUsername(token);
                    } catch (MyJWTException e) {
                        e.printStackTrace();
                    }

                }

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailService.loadUserByUsername(username);
                    try {
                        if (jwtUtil.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                                    null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    } catch (MyJWTException e) {
                        Logger.getLogger(JWTRequestFilter.class.getSimpleName()).info("I Catch Exception !");
                        e.printStackTrace();
                    }
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
