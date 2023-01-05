package com.example.mvcprac.model;

import com.example.mvcprac.dto.account.SignUpForm;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private boolean shopCreatedByWeb = true;

    private boolean shopEnrollmentResultByEmail;

    private boolean shopEnrollmentResultByWeb = true;

    private boolean shopUpdatedByEmail;

    private boolean shopUpdatedByWeb = true;

    private LocalDateTime emailCheckTokenGeneratedAt;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    private LocalDateTime joinedAt;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    /**
     * test create account
     */
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

    /**
     * create account
     */
    public Account(SignUpForm signupDto, String encodePassword, boolean shopCreatedByWeb, boolean shopEnrollmentResultByWeb, boolean shopUpdatedByWeb) {
        this.username = signupDto.getUsername();
        this.nickname = signupDto.getNickname();
        this.password = encodePassword;
        this.birthday = signupDto.getBirthday();
        this.email = signupDto.getEmail();
        this.address = signupDto.getAddress();
        this.phoneNumber = signupDto.getPhoneNumber();
        this.shopCreatedByWeb = shopCreatedByWeb;
        this.shopEnrollmentResultByWeb = shopEnrollmentResultByWeb;
        this.shopUpdatedByWeb = shopUpdatedByWeb;
    }


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

}
