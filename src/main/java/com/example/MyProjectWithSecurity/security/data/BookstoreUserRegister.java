package com.example.MyProjectWithSecurity.security.data;

import com.example.MyProjectWithSecurity.Repositories.UserRepository;
import com.example.MyProjectWithSecurity.data.User;
import com.example.MyProjectWithSecurity.security.repository.BookstoreUserRepository;
import com.example.MyProjectWithSecurity.security.jwt.JWTUtil;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetailService;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookstoreUserRegister {

    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailService bookstoreUserDetailService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailService bookstoreUserDetailService, JWTUtil jwtUtil, UserRepository userRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailService = bookstoreUserDetailService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Регистрация нового пользователя BookstoreUser
     * @param registrationForm
     */
    public void registerNewUser(RegistrationForm registrationForm){
        if(bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()) == null){
            BookstoreUser user = new BookstoreUser();
            user.setEmail(registrationForm.getEmail());
            user.setName(registrationForm.getName());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
            user.setBalance(0.0);
            user.setReg_time(LocalDateTime.now());

            User user_two = new User();
            user_two = userRepository.findUserByEmailContains(user.getEmail());
            if(user_two == null)
                user_two = userRepository.findUserByPhoneContains(user.getPhone());
            if(user_two != null){
                user.setUser(user_two);
            }
            else{
                List<User> list = userRepository.findAll();

                User newUser = new User();
                newUser.setId((list.get(list.size()-1).getId())+1);
                newUser.setBalance(0.0);
                newUser.setUser_name(user.getName());
                newUser.setReg_time(user.getReg_time());
                newUser.setEmail(user.getEmail());
                newUser.setPhone(user.getPhone());
                userRepository.save(newUser);
                user_two = userRepository.findUserByEmailContains(user.getEmail());
                user.setUser(user_two);
            }
            bookstoreUserRepository.save(user);
        }
    }

    /**
     *
     * @param payload
     * @return
     */
    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public Object getCurrentUser() {
        BookstoreUserDetails userDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getBookstoreUser();
    }
}
