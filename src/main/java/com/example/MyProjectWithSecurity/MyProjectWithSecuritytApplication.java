package com.example.MyProjectWithSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.PreDestroy;
import java.util.logging.Logger;

@SpringBootApplication
public class MyProjectWithSecuritytApplication {


    public static void main(String[] args) {

        SpringApplication.run(MyProjectWithSecuritytApplication.class, args);
    }

}
