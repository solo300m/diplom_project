package com.example.MyProjectWithSecurity.security.repository;

import com.example.MyProjectWithSecurity.security.data.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookstoreUserRepository extends JpaRepository<BookstoreUser,Integer> {

    public BookstoreUser findBookstoreUserByEmail(String email);
    public BookstoreUser findBookstoreUsersByIdIs(Integer id);

}
