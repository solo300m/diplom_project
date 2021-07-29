package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Book2Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookTwoGenreRepository extends JpaRepository<Book2Genre,Integer> {
    @Query("from Book2Genre where genreid=:id")
    public List<Book2Genre> selectBookGenreId(@Param("id")Integer id);
    public Page<Book2Genre> findBook2GenreByGenreId(Integer id, Pageable nextPage);

//    @Query("from Book2Genre where genreid=:id")
//    public Page<Book2Genre> selectBook2GenreId(@Param("id")Integer id, Pageable nextPage);
}
