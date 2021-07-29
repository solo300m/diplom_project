package com.example.MyProjectWithSecurity.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
@Table(name = "book_two_author")
public class Book2Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //private Integer book_id;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnore
    private Book book;
    @ManyToOne
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    @JsonIgnore
    private Authors authors;
    //private Integer author_id;
    private Integer sort_index;

    public Book2Author() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSort_index() {
        return sort_index;
    }

    public void setSort_index(Integer sort_index) {
        this.sort_index = sort_index;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Authors getAuthors() {
        return authors;
    }

    public void setAuthors(Authors authors) {
        this.authors = authors;
    }
}
