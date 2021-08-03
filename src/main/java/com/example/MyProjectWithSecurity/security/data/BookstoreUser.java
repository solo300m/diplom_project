package com.example.MyProjectWithSecurity.security.data;

import com.example.MyProjectWithSecurity.data.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class BookstoreUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private LocalDateTime reg_time;
    private Double balance;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bookstoreUser_id", referencedColumnName = "id")
    private User user;

//    @OneToMany(mappedBy = "users")
//    private List<Book_Review> bookReviews = new ArrayList<>();
//
//    @OneToMany(mappedBy = "users")
//    private List<User_Contact> userContacts = new ArrayList<>() ;
//
//    @OneToMany(mappedBy = "users")
//    private List<Book_Review_Like>book_review_likes = new ArrayList<>();
//
//    @OneToMany(mappedBy = "users")
//    private List<Book2User>book2Users = new ArrayList<>();
//
//    @OneToMany(mappedBy = "users")
//    private List<File_Download>file_downloads = new ArrayList<>();
//
//    @OneToMany(mappedBy = "users")
//    private List<Message>messages = new ArrayList<>();
//
//    @OneToMany(mappedBy = "users")
//    private List<Balance_Transaction>balance_transactions = new ArrayList<>();



    public BookstoreUser() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password ;
    }
}
