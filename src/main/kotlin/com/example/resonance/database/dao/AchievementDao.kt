package com.example.resonance.database.dao

import com.example.resonance.database.entity.Achievement
import com.example.resonance.database.entity.Student

interface AchievementDao: AbstractDao<Achievement> {
    fun findAchievementsByStudents(students: Student): MutableList<Achievement>
}