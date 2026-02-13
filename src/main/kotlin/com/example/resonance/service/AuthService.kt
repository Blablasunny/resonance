package com.example.resonance.service

import com.example.resonance.model.schema.dto.AuthDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.AuthRq

interface AuthService {
    fun authenticate(rq: AuthRq): AuthDto
    fun validateToken(token: String): Boolean
    fun register(rq: RegisterRq): RegisterDto
}