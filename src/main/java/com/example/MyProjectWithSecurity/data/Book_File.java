package com.example.MyProjectWithSecurity.data;

import javax.persistence.*;

@Entity
@Table(name = "book_file")
public class Book_File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hash;

    @ManyToOne
    @JoinColumn(name = "type_id",referencedColumnName = "id")
    private Book_File_type book_file_type;
    //private Integer type_id;
    private String path;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public Book_File() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Book_File_type getBook_file_type() {
        return book_file_type;
    }

    public void setBook_file_type(Book_File_type book_file_type) {
        this.book_file_type = book_file_type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    public String getBookFileExtensionString(Integer typeId){
        return Fyle_Type.getExtensionStringByTypeId(typeId);
    }
}
