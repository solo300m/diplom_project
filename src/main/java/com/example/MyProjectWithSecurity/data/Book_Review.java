package com.example.MyProjectWithSecurity.data;

import com.example.MyProjectWithSecurity.security.BookstoreUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "book_review")
public class Book_Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
    //private Integer user_id;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @Type(type="org.hibernate.type.LocalDateTimeType")
    private LocalDateTime time;
    private String text;
    @Type(type = "text")
    private String texttail;
    @OneToMany(mappedBy = "book_review")
    private List<Book_Review_Like>book_review_likes = new ArrayList<>();

    public Book_Review() {
    }

    public String getTextTail() {
        return texttail;
    }

    public void setTextTail(String textTail) {
        this.texttail = textTail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book_Review_Like> getBook_review_likes() {
        return book_review_likes;
    }

    public void setBook_review_likes(List<Book_Review_Like> book_review_likes) {
        this.book_review_likes = book_review_likes;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCountLikeToReview(){
        List<Book_Review_Like> list = new ArrayList<>();
        list = getBook_review_likes().stream().filter(c->c.getValue()==1).collect(Collectors.toList());
        return list.size();
    }
    public Integer getCountDisLikeToReview(){
        List<Book_Review_Like> list = new ArrayList<>();
        list = getBook_review_likes().stream().filter(c->c.getValue()==0).collect(Collectors.toList());
        return list.size();
    }
}
