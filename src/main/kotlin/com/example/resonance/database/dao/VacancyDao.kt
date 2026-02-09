package com.example.resonance.database.dao

import com.example.resonance.database.entity.Company
import com.example.resonance.database.entity.Vacancy

interface VacancyDao: AbstractDao<Vacancy> {
    fun findVacanciesByCompanies(company: Company): MutableList<Vacancy>
}