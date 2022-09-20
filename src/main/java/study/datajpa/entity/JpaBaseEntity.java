package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;


/**
 * 순수 JPA
 * 엔티티가 많아지더라도 이런 식으로 모든 등록 수정일에 대하여 자동화 할 수 있다.
 * JPA 주요 이벤트 어노테이션
 * @PrePersist, @PostPersist
 * @PreUpdate, @PostUpdate
 */
@Getter
@MappedSuperclass
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
