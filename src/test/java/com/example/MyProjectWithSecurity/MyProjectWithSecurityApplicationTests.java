package com.example.MyProjectWithSecurity;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MyProjectWithSecurityApplicationTests {
    @Value("${auth.secret}")
    String authSecret;

    private final MyProjectWithSecuritytApplication application;

    @Autowired
    MyProjectWithSecurityApplicationTests(MyProjectWithSecuritytApplication application) {
        this.application = application;
    }

    @Test
    void contextLoads() {
        assertNotNull(application);
    }
    @Test
    void verifyAuthSecret(){
       assertThat(authSecret,Matchers.containsString("box"));
    }

}
