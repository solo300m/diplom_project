package com.example.MyProjectWithSecurity.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "genre")
public class Genre implements Comparable<Genre> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer parent_id;
    private String slug;
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<Book2Genre>book2Genres = new ArrayList<>();

    public Genre() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Genre o){
        if(this.getId()>o.getId())
            return 1;
        else if(this.getId()==o.getId())
            return 0;
        else
            return -1;
    }


}
