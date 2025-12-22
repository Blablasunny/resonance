package com.example.resonance.service

import com.example.resonance.model.schema.request.UpsertUserRq
import com.example.resonance.model.schema.dto.UserDto

interface UserService {
    fun getUsers() : List<UserDto>
    fun createUser(rq: UpsertUserRq): UserDto
}