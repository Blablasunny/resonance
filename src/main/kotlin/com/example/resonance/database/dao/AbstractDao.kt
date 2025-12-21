package com.example.resonance.database.dao

import com.example.resonance.database.entity.AbstractEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface AbstractDao<T: AbstractEntity>: CrudRepository<T, UUID>