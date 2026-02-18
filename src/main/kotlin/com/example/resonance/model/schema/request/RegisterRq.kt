package com.example.resonance.model.schema.request

import com.example.resonance.database.entity.UserType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRq(
    @field:NotBlank(message = "Не указан email")
    @field:Email(message = "Неверный формат email")
    val email: String,

    @field:NotBlank(message = "Не указан пароль")
    @field:Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
    val password: String,

    @field:NotBlank(message = "Не указан подтверждение пароля")
    val confirmPassword: String,

    val userType: UserType,

    val studentData: UpsertStudentRq? = null,

    val companyData: UpsertCompanyRq? = null
)