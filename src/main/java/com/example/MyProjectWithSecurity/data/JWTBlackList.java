package com.example.MyProjectWithSecurity.data;

import javax.persistence.*;

/**
 * класс JWTBlackList создает таблицу в базе данных black_list
 * для записи туда токенов после выхода из программы по алгоритму /logout
 */
@Entity
@Table(name = "blackList")
public class JWTBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "value")
    private String tokenValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
