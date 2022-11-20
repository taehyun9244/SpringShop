package com.example.mvcprac.model;

import com.example.mvcprac.dto.visa.VisaForm;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "visas")
public class Visa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String country;

    private LocalDateTime createdAt;

    @ManyToOne
    private Account account;

    public Visa(VisaForm visaForm, Account account) {
        this.title = visaForm.getTitle();
        this.body = visaForm.getBody();
        this.country = visaForm.getCountry();
        this.createdAt = LocalDateTime.now();
        this.account = account;
    }
}
