package com.example.resonance.model.schema.dto

data class UpdateEmailDto(
    val message: String,
    val accessToken: String,
    val refreshToken: String,
    val newEmail: String
)