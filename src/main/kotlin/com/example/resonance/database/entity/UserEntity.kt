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
    @Column(name = "user_id", nullable = false)
    var userId: UUID,
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    val userType: UserType,
    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true,
    @Column(name = "email", nullable = false, unique = true)
    val email: String,
    @Column(name = "password", nullable = false)
    val password: String,
) : AbstractEntity()

enum class UserType {
    STUDENT, COMPANY
}

