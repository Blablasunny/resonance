package com.example.resonance.database.entity

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
@Table(name = "achievement")
data class Achievement(
    @Column(name = "achievement_type", nullable = false)
    var achievementType: AchievementType,
    @Column(name = "title", nullable = false)
    var title: String,
    @Lob
    @Column(name = "description")
    var description: String?,
    @Column(name = "results", nullable = false)
    var results: String,
    @Column(name = "confirmation", nullable = false)
    var confirmation: Boolean = false,
    @Column(name = "date", nullable = false)
    var date: LocalDate,
) : AbstractEntity() {
    @ManyToMany
    @JoinTable(
        name = "student_achievement",
        joinColumns = [JoinColumn(name = "achievement_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")])
    var students: MutableSet<Student> = mutableSetOf()

    @PreRemove
    private fun preRemove() {
        students.toList().forEach { it.achievements.remove(this) }
    }
}

enum class AchievementType {
    ARTICLE,         // Статья
    OLYMPIAD,        // Олимпиада
    CONFERENCE,      // Конференция
    ACCELERATOR,     // Акселератор
    STARTUP,         // Стартап
    COURSE,          // Курс
    CERTIFICATION,   // Курс повышения квалификации
    VOLUNTEERING,    // Волонтерство
    CASECOMPETITION, // Кейс-чемпионат
}