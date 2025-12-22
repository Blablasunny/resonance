package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user_entity")
data class UserEntity (
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    val userType: UserType,
    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true,
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "password", nullable = false)
    val password: String,
) : AbstractEntity() {
    @Column(name = "user_id", nullable = false)
    lateinit var userId: UUID
}

enum class UserType {
    STUDENT, COMPANY
}

