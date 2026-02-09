package com.example.resonance.database.dao

import com.example.resonance.database.entity.Responsibility
import com.example.resonance.database.entity.Vacancy

interface ResponsibilityDao: AbstractDao<Responsibility> {
    fun findResponsibilitiesByVacancies(vacancies: Vacancy): MutableList<Responsibility>
}