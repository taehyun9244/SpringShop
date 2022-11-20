package com.example.mvcprac.model;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 64)
    private String series;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(nullable = false, length = 64)
    private String token;

    @Column(name = "last_used", nullable = false, length = 64)
    private LocalDateTime lastUsed;
}
