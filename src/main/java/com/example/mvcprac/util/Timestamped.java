package com.example.mvcprac.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter // 접근제어자인 Private createdAt과 updateAt에 접근하기 위해서
@Setter
@MappedSuperclass // 이것은 생성 수정일자를 모든 엔티티에서 공통으로 가져가야 되므로 추가, 또한 BaseEntity임을 알리기위해서
@EntityListeners(AuditingEntityListener.class)
// 자동적으로 시간을 적용해주며, BaseEntity를 상속받은 곳에서는 AuditingEntityListener를 지정 불필요
public abstract class Timestamped {

    @CreatedDate // 최초 생성 시점
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 변경 시점
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateAt;

}
