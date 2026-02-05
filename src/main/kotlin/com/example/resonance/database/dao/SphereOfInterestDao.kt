package com.example.resonance.database.dao

import com.example.resonance.database.entity.SphereOfInterest
import com.example.resonance.database.entity.Student

interface SphereOfInterestDao: AbstractDao<SphereOfInterest> {
    fun findSphereOfInterestByStudents(students: Student): MutableList<SphereOfInterest>
}