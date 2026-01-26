package com.example.resonance.database.dao

import com.example.resonance.database.entity.UserEntity
import java.util.UUID

interface UserDao: AbstractDao<UserEntity> {
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
}