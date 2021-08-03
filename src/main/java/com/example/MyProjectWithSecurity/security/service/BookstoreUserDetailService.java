package com.example.MyProjectWithSecurity.security.service;

import com.example.MyProjectWithSecurity.security.repository.BookstoreUserRepository;
import com.example.MyProjectWithSecurity.security.data.BookstoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserDetailService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserDetailService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(s);
        if(bookstoreUser != null){
            return new BookstoreUserDetails(bookstoreUser);
        }
        else {
            throw  new UsernameNotFoundException("user not found doh!");
        }
    }
}
