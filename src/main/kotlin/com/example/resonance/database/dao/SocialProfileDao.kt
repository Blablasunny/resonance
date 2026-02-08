package com.example.resonance.database.dao

import com.example.resonance.database.entity.Company
import com.example.resonance.database.entity.SocialProfile
import com.example.resonance.database.entity.Student

interface SocialProfileDao: AbstractDao<SocialProfile> {
    fun findSocialProfilesByStudents(students: Student): List<SocialProfile>
    fun findSocialProfilesByCompanies(companies: Company): List<SocialProfile>
}