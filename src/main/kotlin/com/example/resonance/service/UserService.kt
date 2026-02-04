package com.example.resonance.service

import com.example.resonance.database.entity.UserEntity
import com.example.resonance.model.schema.dto.EmailDto
import com.example.resonance.model.schema.dto.UpdateEmailDto
import com.example.resonance.model.schema.request.EmailRq
import com.example.resonance.model.schema.request.UpdateEmailRq
import com.example.resonance.model.schema.request.UpdatePasswordRq
import java.util.UUID

interface UserService {
    fun getUsers() : List<EmailDto>
    fun getUser(id : UUID) : UserEntity
    fun getId(rq: EmailRq) : UUID
    fun getEmail(id: UUID) : EmailDto
    fun updateEmail(id: UUID, rq: UpdateEmailRq) : UpdateEmailDto
    fun updatePassword(id: UUID, rq: UpdatePasswordRq) : EmailDto
}