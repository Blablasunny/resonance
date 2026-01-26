package com.example.resonance.database.dao

import com.example.resonance.database.entity.Education
import com.example.resonance.database.entity.Student
import java.util.Optional

interface EducationDao: AbstractDao<Education> {
    fun findEducationsByStudents(students: Optional<Student>): MutableList<Education>
}