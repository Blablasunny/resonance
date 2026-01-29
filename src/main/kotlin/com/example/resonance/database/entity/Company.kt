package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Lob
import jakarta.persistence.Table

@Entity
@Table(name = "company")
data class Company (
    @Column(name = "company_name", nullable = false)
    var companyName : String,
    @Lob
    @Column(name = "company_description", nullable = false)
    var companyDescription : String,
    @Column(name = "industry", nullable = false)
    var industry: String,
    @Column(name = "website_link", nullable = false)
    var websiteLink : String,
    @Column(name = "career_page_link", nullable = false)
    var careerPageLink : String,
) : AbstractEntity()