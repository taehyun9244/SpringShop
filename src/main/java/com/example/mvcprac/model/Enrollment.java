package com.example.mvcprac.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Account account;
}
