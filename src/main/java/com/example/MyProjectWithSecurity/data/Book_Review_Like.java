package com.example.MyProjectWithSecurity.data;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
public class Book_Review_Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //private Integer review_id;
    @ManyToOne
    @JoinColumn(name = "review_id",referencedColumnName = "id")
    private Book_Review book_review;
    //private Integer user_id;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @Type(type="org.hibernate.type.LocalDateTimeType")
    private LocalDateTime time;
    private Integer value;

    public Book_Review_Like() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book_Review getBook_review() {
        return book_review;
    }

    public void setBook_review(Book_Review book_review) {
        this.book_review = book_review;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
