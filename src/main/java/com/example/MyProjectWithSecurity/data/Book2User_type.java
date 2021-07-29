package com.example.MyProjectWithSecurity.data;
import javax.persistence.*;

enum Type_Name{
    KEPT(1),
    CART(2),
    PAID(3),
    ARCHIVED(4);

    Type_Name(int i) {
    }
}

@Entity
@Table(name = "book_two_user_type")
public class Book2User_type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private Type_Name name;

    public Book2User_type() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Type_Name getName() {
        return name;
    }

    public void setName(Type_Name name) {
        this.name = name;
    }
}
