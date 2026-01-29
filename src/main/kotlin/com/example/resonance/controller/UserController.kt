package com.example.resonance.controller

import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.schema.dto.UserDto
import com.example.resonance.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
) {
    @GetMapping
    fun getUsers(): List<String> = userService.getUsers()
}