package com.example.resonance.database.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.Lob
import jakarta.persistence.ManyToMany
import jakarta.persistence.PreRemove
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "activity")
data class Activity(
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "activity_type", nullable = false)
    var activityType: ActivityType,
    @Lob
    @Column(name = "format", nullable = false)
    var format: String,
    @Lob
    @Column(name = "description", nullable = false)
    var description: String,
    @Lob
    @Column(name = "spheres", nullable = false)
    var spheres: String,
    @Column(name = "date", nullable = false)
    var date: LocalDate,
): AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "company_activities",
        joinColumns = [JoinColumn(name = "activity_id")],
        inverseJoinColumns = [JoinColumn(name = "company_id")])
    var companies: MutableSet<Company> = mutableSetOf()

    @PreRemove
    fun preRemove() {
        companies.toList().forEach { it.activities.remove(this) }
    }
}

@Schema(description = "Типы активностей от компаний")
enum class ActivityType {
    @Schema(description = "Стажировка")
    INTERNSHIP,

    @Schema(description = "Кейс-чемпионат")
    CASECOMPETITION,

    @Schema(description = "Карьерные мероприятия")
    CAREEREVENTS,

    @Schema(description = "Обучающие мероприятия")
    TRAININGEVENTS,

    @Schema(description = "Конкурс")
    CONTEST
}
