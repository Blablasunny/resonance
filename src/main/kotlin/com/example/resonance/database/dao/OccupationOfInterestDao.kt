package com.example.resonance.database.dao

import com.example.resonance.database.entity.OccupationOfInterest
import com.example.resonance.database.entity.Student

interface OccupationOfInterestDao: AbstractDao<OccupationOfInterest>  {
    fun findOccupationOfInterestByStudents(students: Student): MutableList<OccupationOfInterest>
}