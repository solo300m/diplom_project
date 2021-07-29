package com.example.MyProjectWithSecurity.data;

import com.example.MyProjectWithSecurity.security.BookstoreUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//@MappedSuperclass
@Table(name="customer")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name = "BD_TYPE")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //private String hash;
    @Type(type="org.hibernate.type.LocalDateTimeType")
    private LocalDateTime reg_time;
    private Double balance;
    private String user_name;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<BookRating> bookRating = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private BookstoreUser bookstoreUser;

    @OneToMany(mappedBy = "user")
    private List<Book_Review>bookReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<User_Contact> userContacts = new ArrayList<>() ;

    @OneToMany(mappedBy = "user")
    private List<Book_Review_Like>book_review_likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Book2User>book2Users = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<File_Download>file_downloads = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Message>messages = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Balance_Transaction>balance_transactions = new ArrayList<>();

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    //    public String getHash() {
//        return hash;
//    }
//
//    public void setHash(String hash) {
//        this.hash = hash;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getReg_time() {
        return reg_time;
    }

    public void setReg_time(LocalDateTime reg_time) {
        this.reg_time = reg_time;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public List<User_Contact> getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(List<User_Contact> userContacts) {
        this.userContacts = userContacts;
    }

    public List<Book_Review> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<Book_Review> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public List<Book_Review_Like> getBook_review_likes() {
        return book_review_likes;
    }

    public void setBook_review_likes(List<Book_Review_Like> book_review_likes) {
        this.book_review_likes = book_review_likes;
    }

    public List<Book2User> getBook2Users() {
        return book2Users;
    }

    public void setBook2Users(List<Book2User> book2Users) {
        this.book2Users = book2Users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<File_Download> getFile_downloads() {
        return file_downloads;
    }

    public void setFile_downloads(List<File_Download> file_downloads) {
        this.file_downloads = file_downloads;
    }

    public List<Balance_Transaction> getBalance_transactions() {
        return balance_transactions;
    }

    public void setBalance_transactions(List<Balance_Transaction> balance_transactions) {
        this.balance_transactions = balance_transactions;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
