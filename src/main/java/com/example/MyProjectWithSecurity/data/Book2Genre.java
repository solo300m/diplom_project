package com.example.MyProjectWithSecurity.data;

import javax.persistence.*;

@Entity
@Table(name = "book_two_genre")
public class Book2Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //private Integer book_id;
    @ManyToOne
    @JoinColumn(name = "bookid",referencedColumnName = "id")
    private Book book;
    //private Integer genre_id;

    @ManyToOne
    @JoinColumn(name = "genreid",referencedColumnName = "id")
    private Genre genre;


    public Integer getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
