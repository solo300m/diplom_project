package com.example.MyProjectWithSecurity.security.repository;

import com.example.MyProjectWithSecurity.data.JWTBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JWTBlackListRepository extends JpaRepository<JWTBlackList,Integer> {


    public JWTBlackList findJWTBlackListByTokenValue(String token);

}
