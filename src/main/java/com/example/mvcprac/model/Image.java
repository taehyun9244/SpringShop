package com.example.mvcprac.model;

import com.example.mvcprac.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    //고객이 업로드한 파일명
    private String uploadFileName;

    @Column(nullable = false)
    ////서버에 저장하는 파일명
    private String storeFileName;

    public Image(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
