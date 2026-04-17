package com.example.resonance.service

import com.example.resonance.model.schema.dto.AuthResponseDto
import com.example.resonance.model.schema.dto.RefreshTokenResponseDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.AuthRq
import com.example.resonance.model.schema.request.RefreshTokenRq
import jakarta.servlet.http.HttpServletRequest
import java.util.UUID

interface AuthService {
    fun authenticate(rq: AuthRq, request: HttpServletRequest): AuthResponseDto
    fun validateToken(token: String): Boolean
    fun register(rq: RegisterRq, request: HttpServletRequest): RegisterDto
    fun refreshToken(rq: RefreshTokenRq, request: HttpServletRequest): RefreshTokenResponseDto
    fun logout(token: String, refreshToken: String?)
    fun logoutAll(userId: UUID)
}