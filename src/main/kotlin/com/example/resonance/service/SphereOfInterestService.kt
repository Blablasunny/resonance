package com.example.resonance.service

import com.example.resonance.database.entity.SphereOfInterest
import com.example.resonance.model.schema.dto.SphereOfInterestDto
import com.example.resonance.model.schema.request.UpsertSphereOfInterestRq
import java.util.UUID

interface SphereOfInterestService {
    fun getSphereOfInterestByStudentId(studentId: UUID): List<SphereOfInterestDto>
    fun getSphereOfInterest(id: UUID): SphereOfInterest
    fun getSphereOfInterests(): List<SphereOfInterestDto>
    fun addSphereOfInterest(studentId: UUID, rq: UpsertSphereOfInterestRq): SphereOfInterestDto
    fun changeSphereOfInterest(id: UUID, rq: UpsertSphereOfInterestRq, studentId: UUID): SphereOfInterestDto
    fun deleteSphereOfInterest(id: UUID, studentId: UUID)
}