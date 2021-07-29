package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Book_Review_Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReviewLikeRepository extends JpaRepository <Book_Review_Like, Integer> {

    public List<Book_Review_Like> findBook_Review_LikesById(Integer id);

}
