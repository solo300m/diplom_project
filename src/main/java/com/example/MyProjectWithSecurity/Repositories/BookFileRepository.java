package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Book_File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<Book_File,Integer> {

    public Book_File findBook_FileByHash(String hash);
}
