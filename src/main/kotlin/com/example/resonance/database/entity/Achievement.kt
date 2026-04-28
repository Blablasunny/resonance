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

@Schema(description = "Типы достижений студентов")
enum class AchievementType {
    @Schema(description = "Публикация статьи")
    ARTICLE,

    @Schema(description = "Участие в олимпиаде")
    OLYMPIAD,

    @Schema(description = "Участие в конференции")
    CONFERENCE,

    @Schema(description = "Участие в акселераторе")
    ACCELERATOR,

    @Schema(description = "Собственный стартап")
    STARTUP,

    @Schema(description = "Пройденный курс")
    COURSE,

    @Schema(description = "Сертификация")
    CERTIFICATION,

    @Schema(description = "Волонтерство")
    VOLUNTEERING,

    @Schema(description = "Кейс-чемпионат")
    CASECOMPETITION
}