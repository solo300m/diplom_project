package com.example.MyProjectWithSecurity.data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

enum Fyle_Type{
    PDF(".pdf"),
    EPUB(".epub"),
    FB2(".fb2");
    private final String fileExtensionString;

    Fyle_Type(String fileExtensionString) {
        this.fileExtensionString = fileExtensionString;
    }
    public static String getExtensionStringByTypeId(Integer typeId){
        switch (typeId){
            case 1: return Fyle_Type.PDF.fileExtensionString;

            case 2: return Fyle_Type.EPUB.fileExtensionString;

            case 3: return Fyle_Type.FB2.fileExtensionString;

            default:return "";
        }
    }
}
@Entity
@Table(name = "book_file_type")
public class Book_File_type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Fyle_Type name;
    @Type(type = "text")
    private String discription;
    @OneToMany(mappedBy = "book_file_type")
    private List<Book2User>book2Users = new ArrayList<>();
    @OneToMany(mappedBy = "book_file_type")
    private List<Book_File> book_files = new ArrayList<>();

    public Book_File_type() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Fyle_Type getName() {
        return name;
    }

    public void setName(Fyle_Type name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
