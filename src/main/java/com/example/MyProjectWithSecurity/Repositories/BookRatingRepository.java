package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    public List<BookRating> findBookRatingsByBookIdAndRating(Integer bookId, Integer rating);

}
