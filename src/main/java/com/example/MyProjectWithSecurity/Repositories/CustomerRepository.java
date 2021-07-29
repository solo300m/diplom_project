package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<User,Integer> {
    @Query("from User")
    List<User> selectAllCustomers();
}
