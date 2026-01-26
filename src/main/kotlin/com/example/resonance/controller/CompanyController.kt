package com.example.resonance.controller

import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto
import com.example.resonance.service.CompanyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyController(
    private val service: CompanyService,
) {
    @GetMapping
    fun getCompanies(): List<CompanyDto> = service.getCompanies()

//    @PostMapping
//    fun create(@RequestBody rq: UpsertCompanyRq) = service.createCompany(rq)
}