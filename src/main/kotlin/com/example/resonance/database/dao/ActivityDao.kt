package com.example.resonance.database.dao

import com.example.resonance.database.entity.Activity
import com.example.resonance.database.entity.Company

interface ActivityDao: AbstractDao<Activity> {
    fun findActivitiesByCompanies(companies: Company): MutableList<Activity>
}