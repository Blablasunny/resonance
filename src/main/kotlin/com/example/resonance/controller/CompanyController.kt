package com.example.resonance.controller

import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto
import com.example.resonance.model.schema.request.UpsertActivityRq
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.service.ActivityService
import com.example.resonance.service.CompanyService
import org.springframework.security.access.prepost.PreAuthorize
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
    private val activityService: ActivityService,
) {
    @GetMapping
    fun getCompanies(): List<CompanyDto> = companyService.getCompanies()

    @GetMapping("/{id}")
    fun getCompany(@PathVariable("id") id: UUID) = companyService.getCompany(id).toDto()

    @PreAuthorize("@securityService.isCompanyOwner(#id)")
    @PostMapping("/{id}")
    fun updateCompany(@PathVariable("id") id: UUID, @RequestBody rq: UpsertCompanyRq) =
        companyService.updateCompany(id, rq)

    @PreAuthorize("@securityService.isCompanyOwner(#id)")
    @DeleteMapping("/{id}")
    fun deleteCompany(@PathVariable("id") id: UUID) = companyService.deleteCompany(id)


    @GetMapping("/activities/{companyId}")
    fun getActivityByCompanyId(@PathVariable("companyId") companyId: UUID) =
        activityService.getActivityByCompanyId(companyId)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @PostMapping("/activities/{companyId}")
    fun addActivity(@PathVariable("companyId") companyId: UUID, @RequestBody rq: UpsertActivityRq) =
        activityService.addActivity(companyId, rq)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @PostMapping("/activities/{companyId}/{id}")
    fun changeActivity(@PathVariable("companyId") companyId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertActivityRq) =
        activityService.changeActivity(id, rq, companyId)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @DeleteMapping("/activities/{companyId}/{id}")
    fun changeActivity(@PathVariable("companyId") companyId: UUID, @PathVariable("id") id: UUID) =
        activityService.deleteActivity(id, companyId)
}