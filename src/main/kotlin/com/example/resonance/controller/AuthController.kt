package com.example.resonance.controller

import com.example.resonance.model.schema.dto.AuthDto
import com.example.resonance.model.schema.dto.RegisterDto
import com.example.resonance.model.schema.request.RegisterRq
import com.example.resonance.model.schema.request.UpsertAuthRq
import com.example.resonance.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: UpsertAuthRq): ResponseEntity<AuthDto> {
        val response = authService.authenticate(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRq): ResponseEntity<RegisterDto> {
        val response = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/validate")
    fun validateToken(@RequestHeader("Authorization") token: String): ResponseEntity<Map<String, Any>> {
        val isValid = authService.validateToken(token.replace("Bearer ", ""))
        return ResponseEntity.ok(mapOf("valid" to isValid))
    }
}