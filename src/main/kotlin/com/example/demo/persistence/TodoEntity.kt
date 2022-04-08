package com.example.demo.persistence

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Table(name = "todo")
@Entity(name = "todo")
class TodoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    var task: String,

    var completed: Boolean,
)

interface TodoRepository: JpaRepository<TodoEntity, Int> {
    fun findAllByCompleted(completed: Boolean): List<TodoEntity>
    fun deleteAllByCompleted(completed: Boolean)
    fun findByTaskIsContainingIgnoreCase(partialTask: String): List<TodoEntity>
}
