package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findUserByIdIs(Integer id);
    public User findUserByEmailContains(String email);
    public User findUserByPhoneContains(String phone);
    public List<User> findAll();
}
