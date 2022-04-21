package com.example.demo.persistence

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*
import javax.transaction.Transactional

@Table(name = "todo")
@Entity(name = "todo")
class TodoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "task")
    var task: String,

    var completed: Boolean,
): AuditingEntity()

interface TodoRepository : JpaRepository<TodoEntity, Int> {
    // these are functions that follow a certain naming convention to automatically generate SQL queries from them
    // see: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    @Transactional
    fun findAllByCompleted(completed: Boolean): List<TodoEntity>
    fun deleteAllByCompleted(completed: Boolean)

    /**
     * will generate SQL-query behind the scenes:
     * select * from todo where upper(task) like upper(?) escape ?
     */
    fun findAllByTaskContainsIgnoreCase(task: String): List<TodoEntity>
}
