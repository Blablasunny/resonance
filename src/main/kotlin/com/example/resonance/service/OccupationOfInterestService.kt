package com.example.resonance.service

import com.example.resonance.database.entity.OccupationOfInterest
import com.example.resonance.model.schema.dto.OccupationOfInterestDto
import com.example.resonance.model.schema.request.UpsertOccupationOfInterestRq
import java.util.UUID

interface OccupationOfInterestService {
    fun getOccupationOfInterestByStudentId(studentId: UUID): List<OccupationOfInterestDto>
    fun getOccupationOfInterest(id: UUID): OccupationOfInterest
    fun getOccupationOfInterests(): List<OccupationOfInterestDto>
    fun addOccupationOfInterest(studentId: UUID, rq: UpsertOccupationOfInterestRq): OccupationOfInterestDto
    fun changeOccupationOfInterest(id: UUID, rq: UpsertOccupationOfInterestRq, studentId: UUID): OccupationOfInterestDto
    fun deleteOccupationOfInterest(id: UUID, studentId: UUID)
}