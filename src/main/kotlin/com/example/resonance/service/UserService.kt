package com.example.resonance.service

import com.example.resonance.database.dao.UserProtection
import com.example.resonance.database.entity.UserEntity
import com.example.resonance.model.schema.request.UpsertUserRq
import com.example.resonance.model.schema.dto.UserDto
import java.util.UUID

interface UserService {
    fun getUsers() : List<String>
    fun getUser(id : UUID) : UserEntity
    fun getEmail(id: UUID) : String
    fun updateEmail(id: UUID, email: String) : String
    fun createUser(rq: UpsertUserRq): UserDto
}