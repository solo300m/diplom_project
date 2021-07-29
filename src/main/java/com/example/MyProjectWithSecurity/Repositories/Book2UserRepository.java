package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book2User;
import com.example.MyProjectWithSecurity.data.User;
import com.example.MyProjectWithSecurity.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Book2UserRepository extends JpaRepository<Book2User, Integer> {
    public void deleteByBookIsNull();
    public void deleteByBookIs(Book book);
    public List<Book2User> findBook2UsersByBookIs(Book book);
    public List<Book2User> findBook2UsersByUser(User user);
}
