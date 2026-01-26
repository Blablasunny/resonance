package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@MappedSuperclass
abstract class AbstractEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null

    @Column(name = "created_at")
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    @UpdateTimestamp
    var updatedAt: LocalDateTime = LocalDateTime.now()
}