package com.example.resonance.model.schema.dto

data class UpdateEmailDto(
    val message: String,
    val newToken: String,
    val newEmail: String
)