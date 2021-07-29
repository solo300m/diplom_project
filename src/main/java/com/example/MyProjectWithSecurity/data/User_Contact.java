package com.example.MyProjectWithSecurity.data;


import com.example.MyProjectWithSecurity.security.BookstoreUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
enum EMUN{
    phone,
    email
}

//@Embeddable
@Entity
//@DiscriminatorValue("id")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "user_contact")
public class User_Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    private EMUN type;

    @Column(nullable = false)
    private String kode;

    private Byte approved;

    private Integer code_trials;
    @Type(type="org.hibernate.type.LocalDateTimeType")
    private LocalDateTime code_time;

    private String contact;

    public User_Contact() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Byte getApproved() {
        return approved;
    }

    public void setApproved(Byte approved) {
        this.approved = approved;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public EMUN getType() {
        return type;
    }

    public void setType(EMUN type) {
        this.type = type;
    }

    public Integer getCode_trials() {
        return code_trials;
    }

    public void setCode_trials(Integer code_trials) {
        this.code_trials = code_trials;
    }

    public LocalDateTime getCode_time() {
        return code_time;
    }

    public void setCode_time(LocalDateTime code_time) {
        this.code_time = code_time;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    @Override
    public String toString() {
        return "User_Contact{" +
                "id=" + id +
                ", user=" + user.getUser_name() +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", type=" + type +
                ", kode='" + kode + '\'' +
                ", approved=" + approved +
                ", code_trials=" + code_trials +
                ", code_time=" + code_time +
                ", contact='" + contact + '\'' +
                '}';
    }
}
