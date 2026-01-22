package com.example.resonance.service

import com.example.resonance.model.schema.dto.AuthDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.UpsertAuthRq

interface AuthService {
    fun authenticate(request: UpsertAuthRq): AuthDto
    fun validateToken(token: String): Boolean
    fun register(request: RegisterRq): RegisterDto
}