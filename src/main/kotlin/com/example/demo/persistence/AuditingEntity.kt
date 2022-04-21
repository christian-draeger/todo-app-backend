package com.example.demo.persistence

import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*
import javax.persistence.*


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditingEntity {

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    var createdDate: Date = Date()

    @Column(name = "modified_date")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    var modifiedDate: Date = Date()

    @PrePersist
    fun onPrePersist() {
        println("this method will be executed everytime before this entity will be persisted")
    }

    @PreUpdate
    fun onPreUpdate() {
        println("this method will be executed everytime before this entity will be updated")
    }

    @PreRemove
    fun onPreRemove() {
        println("this method will be executed everytime before this entity will be deleted")
    }
}

@Configuration
@EnableJpaAuditing
class PersistenceConfig
