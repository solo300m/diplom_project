package com.example.MyProjectWithSecurity.security;


import com.example.MyProjectWithSecurity.security.jwt.JWTRequestFilter;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private  final BookstoreUserDetailService bookstoreUserDetailService;
    private final JWTRequestFilter filter;

    @Autowired
    public SecurityConfig(BookstoreUserDetailService bookstoreUserDetailService, JWTRequestFilter filter){
        this.bookstoreUserDetailService = bookstoreUserDetailService;
        this.filter = filter;
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(bookstoreUserDetailService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile").authenticated()//hasRole("USER") для токенов JWT //Параметр .authenticated()-для настройки сесии с facebook
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/signin").failureUrl("/signin")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/signin").deleteCookies("token")//эта строка добавляется если используются токены вместо сессий

                //два параметра ниже нужны для настройки работы с OAUTH2 идентификация через Facebook
                .and().oauth2Login()
                .and().oauth2Client();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
