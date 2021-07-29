package com.example.MyProjectWithSecurity.data;

import com.example.MyProjectWithSecurity.security.BookstoreUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "book_two_user")
public class Book2User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Type(type="org.hibernate.type.LocalDateTimeType")
    private LocalDateTime time;
    //private Integer type_id;
    @ManyToOne
    @JoinColumn(name = "type_id",referencedColumnName = "id")
    private Book2User_type book_file_type;
    //private Integer book_id;
    @ManyToOne
    @JoinColumn(name = "book_id",referencedColumnName = "id")
    private Book book;
    //private Integer user_id;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    public Book2User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Book2User_type getBook_file_type() {
        return book_file_type;
    }

    public void setBook_file_type(Book2User_type book_file_type) {
        this.book_file_type = book_file_type;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
