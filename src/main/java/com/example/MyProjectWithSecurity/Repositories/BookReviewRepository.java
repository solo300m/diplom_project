package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book_Review;
import com.example.MyProjectWithSecurity.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewRepository extends JpaRepository<Book_Review, Integer> {
    public List<Book_Review> findBook_ReviewsByBookIs(Book book);
    public Book_Review findBook_ReviewById(Integer id);
}
