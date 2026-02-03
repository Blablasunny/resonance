package com.example.resonance.service

import com.example.resonance.database.entity.UserEntity
import com.example.resonance.model.schema.dto.UpdateEmailDto
import com.example.resonance.model.schema.request.UpsertUserRq
import com.example.resonance.model.schema.dto.UserDto
import com.example.resonance.model.schema.request.UpdateEmailRq
import com.example.resonance.model.schema.request.UpdatePasswordRq
import java.util.UUID

interface UserService {
    fun getUsers() : List<String>
    fun getUser(id : UUID) : UserEntity
    fun getId(email: String) : UUID
    fun getEmail(id: UUID) : String
    fun updateEmail(id: UUID, rq: UpdateEmailRq) : UpdateEmailDto
    fun updatePassword(id: UUID, rq: UpdatePasswordRq) : String
}