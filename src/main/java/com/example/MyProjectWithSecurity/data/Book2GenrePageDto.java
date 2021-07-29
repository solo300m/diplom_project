package com.example.MyProjectWithSecurity.data;

import java.util.List;

public class Book2GenrePageDto {
    private Integer count;
    private List<Book2Genre>book2GenreList;

    public Book2GenrePageDto() {
    }

    public Book2GenrePageDto(List<Book2Genre> book2GenreList) {
        this.book2GenreList = book2GenreList;
        this.count = book2GenreList.size();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Book2Genre> getBook2GenreList() {
        return book2GenreList;
    }

    public void setBook2GenreList(List<Book2Genre> book2GenreList) {
        this.book2GenreList = book2GenreList;
    }
}
