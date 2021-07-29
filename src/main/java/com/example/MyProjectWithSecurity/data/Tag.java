package com.example.MyProjectWithSecurity.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tegs")
@ApiModel("data model of tegs entity")
public class Tag {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idteg;
    private String tegname;
    @OneToMany(mappedBy = "tag")
    @JsonIgnore
    private List<Book>books = new ArrayList<>();

    public Integer getIdTeg() {
        return idteg;
    }

    public String getTegName() {
        return tegname;
    }

    public void setTegName(String tegName) {
        this.tegname = tegName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
