package com.example.resonance.database.dao

import com.example.resonance.database.entity.Achievement
import com.example.resonance.database.entity.Student
import java.util.Optional

interface AchievementDao: AbstractDao<Achievement> {
    fun findAchievementsByStudents(students: Optional<Student>): MutableList<Achievement>
}