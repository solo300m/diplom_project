package com.example.MyProjectWithSecurity.data_test;

public class Book2Authors {
    private Integer id;
    private Integer id_author;
    private String author;
    private String title;

    public Book2Authors() {
    }

    public Book2Authors(Integer id, Integer id_author, String author, String title) {
        this.id = id;
        this.id_author = id_author;
        this.author = author;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_author() {
        return id_author;
    }

    public void setId_author(Integer id_author) {
        this.id_author = id_author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return  "id= " + id +
                ", id_author= " + id_author +
                ", author= " + author +
                ", title= " + title ;
    }
}
