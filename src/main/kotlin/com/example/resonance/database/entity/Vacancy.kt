package com.example.resonance.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.Lob
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table

@Entity
@Table(name = "vacancy")
data class Vacancy(
    @Column(name = "title", nullable = false)
    var title: String,
    @Lob
    @Column(name = "description", nullable = false)
    var description: String,
    @Lob
    @Column(name = "requirements", nullable = false)
    var requirements: String,
    @Lob
    @Column(name = "experience", nullable = false)
    var experience: String,
    @Column(name = "is_open", nullable = false)
    var isOpen: Boolean = true,
): AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "company_vacancies",
        joinColumns = [JoinColumn(name = "vacancy_id")],
        inverseJoinColumns = [JoinColumn(name = "company_id")])
    var companies: MutableSet<Company> = mutableSetOf()

    @ManyToMany(mappedBy = "vacancies")
    var skills: MutableSet<Skill> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        companies.toList().forEach { it.vacancies.remove(this) }
        skills.toList().forEach { it.vacancies.remove(this) }
    }
}
