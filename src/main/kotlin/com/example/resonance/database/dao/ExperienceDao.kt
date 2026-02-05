package com.example.resonance.database.dao

import com.example.resonance.database.entity.Experience
import com.example.resonance.database.entity.Student
import java.util.Optional

interface ExperienceDao: AbstractDao<Experience> {
    fun findExperiencesByStudents(students: Student): MutableList<Experience>
}