package com.example.resonance.controller

import com.example.resonance.model.schema.dto.AuthResponseDto
import com.example.resonance.model.schema.dto.LogoutResponseDto
import com.example.resonance.model.schema.dto.RefreshTokenResponseDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.AuthRq
import com.example.resonance.model.schema.request.RefreshTokenRq
import com.example.resonance.security.JwtUtil
import com.example.resonance.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun login(
        @RequestBody rq: AuthRq,
        request: HttpServletRequest
    ): ResponseEntity<AuthResponseDto> {
        val response = authService.authenticate(rq, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody rq: RegisterRq,
        request: HttpServletRequest
    ): ResponseEntity<RegisterDto> {
        val response = authService.register(rq, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @Valid @RequestBody rq: RefreshTokenRq,
        request: HttpServletRequest
    ): ResponseEntity<RefreshTokenResponseDto> {
        val response = authService.refreshToken(rq, request)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody(required = false) body: Map<String, String>?
    ): ResponseEntity<LogoutResponseDto> {
        val token = authHeader.replace("Bearer ", "")
        val refreshToken = body?.get("refreshToken")

        authService.logout(token, refreshToken)

        return ResponseEntity.ok(
            LogoutResponseDto(
                message = "Вы вышли из аккаунт",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout/all")
    fun logoutAll(
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<LogoutResponseDto> {
        val token = authHeader.replace("Bearer ", "")

        val userId = jwtUtil.extractUserId(token)
            ?: throw IllegalArgumentException("Невалидный токен")

        authService.logoutAll(userId)

        return ResponseEntity.ok(
            LogoutResponseDto(
                message = "Вы вышли из аккаунта на всех устройствах",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/validate")
    fun validateToken(
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Map<String, Any>> {
        val token = authHeader.replace("Bearer ", "")
        val isValid = authService.validateToken(token)
        return ResponseEntity.ok(mapOf("valid" to isValid))
    }
}