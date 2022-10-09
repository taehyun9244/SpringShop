package com.example.mvcprac.model;

import com.example.mvcprac.dto.account.SignUpForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String birthday;


    private boolean emailVerified;

    private String emailCheckToken;
    
    private boolean shopCreatedByEmail;

    private boolean shopCreatedByWeb;

    private boolean shopEnrollmentResultByEmail;

    private boolean shopEnrollmentResultByWeb;

    private boolean shopUpdatedByEmail;

    private boolean shopUpdatedByWeb;

    public Account(Long id, String username, String nickname, String password, String email, String address, String phoneNumber, String birthday) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public Account(SignUpForm signupDto, String encodePassword, boolean b, boolean b1, boolean b2) {
        this.username = signupDto.getUsername();
        this.nickname = signupDto.getNickname();
        this.password = encodePassword;
        this.birthday = signupDto.getBirthday();
        this.email = signupDto.getEmail();
        this.address = signupDto.getAddress();
        this.phoneNumber = signupDto.getPhoneNumber();
        this.shopCreatedByWeb = b;
        this.shopEnrollmentResultByWeb = b1;
        this.shopUpdatedByWeb = b2;
    }

    public void generateEmailCheckToke() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void completeSignUp() {
        this.emailVerified = true;
    }
}