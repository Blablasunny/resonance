package com.example.resonance.controller

import com.example.resonance.database.entity.UserType
import com.example.resonance.model.mapper.toDto
import com.example.resonance.model.schema.request.UpsertCompanyRq
import com.example.resonance.model.schema.dto.CompanyDto
import com.example.resonance.model.schema.request.UpsertActivityRq
import com.example.resonance.model.schema.request.UpsertSkillRq
import com.example.resonance.model.schema.request.UpsertSocialProfileRq
import com.example.resonance.model.schema.request.UpsertStudentRq
import com.example.resonance.model.schema.request.UpsertVacancyRq
import com.example.resonance.service.ActivityService
import com.example.resonance.service.CompanyService
import com.example.resonance.service.SkillOwner
import com.example.resonance.service.SkillService
import com.example.resonance.service.SocialProfileService
import com.example.resonance.service.VacancyService
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
    private val socialProfileService: SocialProfileService,
    private val vacancyService: VacancyService,
    private val skillService: SkillService,
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
    fun deleteActivity(@PathVariable("companyId") companyId: UUID, @PathVariable("id") id: UUID) =
        activityService.deleteActivity(id, companyId)


    @GetMapping("/social-profiles/{companyId}")
    fun getSocialProfileByStudentId(@PathVariable("companyId") companyId: UUID) =
        socialProfileService.getSocialProfileByCompanyId(companyId)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @PostMapping("/social-profiles/{companyId}")
    fun addSocialProfile(@PathVariable("companyId") companyId: UUID, @RequestBody rq: UpsertSocialProfileRq) =
        socialProfileService.addSocialProfile(companyId, rq, UserType.COMPANY)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @PostMapping("/social-profiles/{companyId}/{id}")
    fun changeSocialProfile(@PathVariable("companyId") companyId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertSocialProfileRq) =
        socialProfileService.changeSocialProfile(id, rq, companyId, UserType.COMPANY)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @DeleteMapping("/social-profiles/{companyId}/{id}")
    fun deleteSocialProfile(@PathVariable("companyId") companyId: UUID, @PathVariable("id") id: UUID) =
        socialProfileService.deleteSocialProfile(id, companyId, UserType.COMPANY)


    @GetMapping("/vacancies/{companyId}")
    fun getVacancyByCompanyId(@PathVariable("companyId") companyId: UUID) =
        vacancyService.getVacancyByCompanyId(companyId)

    @GetMapping("/vacancies/open/{companyId}")
    fun getOpenVacancyByCompanyId(@PathVariable("companyId") companyId: UUID) =
        vacancyService.getOpenVacancyByCompanyId(companyId)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @PostMapping("/vacancies/{companyId}")
    fun addVacancy(@PathVariable("companyId") companyId: UUID, @RequestBody rq: UpsertVacancyRq) =
        vacancyService.addVacancy(companyId, rq)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @PostMapping("/vacancies/{companyId}/{id}")
    fun changeVacancy(@PathVariable("companyId") companyId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertVacancyRq) =
        vacancyService.changeVacancy(id, rq, companyId)

    @PreAuthorize("@securityService.isCompanyOwner(#companyId)")
    @DeleteMapping("/vacancies/{companyId}/{id}")
    fun deleteVacancy(@PathVariable("companyId") companyId: UUID, @PathVariable("id") id: UUID) =
        vacancyService.deleteVacancy(id, companyId)


    @GetMapping("/skills/{vacancyId}")
    fun getSkillByVacancyId(@PathVariable("vacancyId") vacancyId: UUID) =
        skillService.getSkillByVacancyId(vacancyId)

    @PreAuthorize("@securityService.isVacancyOwner(#vacancyId)")
    @PostMapping("/skills/{vacancyId}")
    fun addSkill(@PathVariable("vacancyId") vacancyId: UUID, @RequestBody rq: UpsertSkillRq) =
        skillService.addSkill(vacancyId, rq, SkillOwner.VACANCY)

    @PreAuthorize("@securityService.isVacancyOwner(#vacancyId)")
    @PostMapping("/skills/{vacancyId}/{id}")
    fun changeSkill(@PathVariable("vacancyId") vacancyId: UUID, @PathVariable("id") id: UUID, @RequestBody rq: UpsertSkillRq) =
        skillService.changeSkill(id, rq, vacancyId, SkillOwner.VACANCY)

    @PreAuthorize("@securityService.isVacancyOwner(#vacancyId)")
    @DeleteMapping("/skills/{vacancyId}/{id}")
    fun deleteSkill(@PathVariable("vacancyId") vacancyId: UUID, @PathVariable("id") id: UUID) =
        skillService.deleteSkill(id, vacancyId, SkillOwner.VACANCY)
}