package com.example.resonance.controller

import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.service.CompanyService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/companies")
class CompanyController(
    private val companyService: CompanyService,
) {
    @GetMapping
    fun getCompanies(): List<CompanyDto> = companyService.getCompanies()

    @GetMapping("/{id}")
    fun getCompany(@PathVariable("id") id: UUID) = companyService.getCompany(id).toDto()

    @PostMapping("/{id}")
    fun updateCompany(@PathVariable("id") id: UUID, @RequestBody rq: UpsertCompanyRq) =
        companyService.updateCompany(id, rq)

    @DeleteMapping("/{id}")
    fun deleteCompany(@PathVariable("id") id: UUID) = companyService.deleteCompany(id)
}