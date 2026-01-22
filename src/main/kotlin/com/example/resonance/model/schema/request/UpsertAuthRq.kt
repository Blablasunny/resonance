package com.example.resonance.model.schema.request

data class UpsertAuthRq(
    val email: String,
    val password: String
)