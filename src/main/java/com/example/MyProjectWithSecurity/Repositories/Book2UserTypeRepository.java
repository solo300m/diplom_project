package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Book2User_type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserTypeRepository extends JpaRepository<Book2User_type, Integer> {
    public Book2User_type findBook2User_typeByCode(String code);
}
