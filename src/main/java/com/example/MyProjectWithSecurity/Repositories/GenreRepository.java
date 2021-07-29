package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre,Integer> {
    public List<Genre> findAll();

    @Query("from Genre where id=:gen")
    public Genre selectGenreBy(@Param("gen") Integer gen);
}

