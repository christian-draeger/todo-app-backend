package com.example.demo.persistence

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class TodoRepositoryTest(
    @Autowired private val todoRepository: TodoRepository
) {

    private fun aTodo(
        task: String = "a sample task",
        completed: Boolean = false
    ) = TodoEntity(
        task = task,
        completed = completed
    )

    private val testData = listOf(
        aTodo(task = "cleaning up", completed = true),
        aTodo(task = "cooking a meal"),
        aTodo(task = "coding kotlin"),
    )

    @Test
    fun `can read a todo`() {
        val testData = todoRepository.saveAll(testData)

        val testDataTodo = todoRepository.findByIdOrNull(testData[0].id)
        Assertions.assertThat(testDataTodo?.task).isEqualTo("cleaning up")
        Assertions.assertThat(testDataTodo?.completed).isTrue
    }

    @Test
    fun `can store a todo`() {
        val saved = todoRepository.save(aTodo())
        Assertions.assertThat(todoRepository.findByIdOrNull(saved.id)).isNotNull
    }

    @Test
    fun `can remove a todo`() {
        val testData = todoRepository.saveAll(testData)

        todoRepository.delete(testData[1])

        Assertions.assertThat(todoRepository.findAll()).containsExactly(
            testData[0],
            testData[2],
        )
    }

    @Test
    fun `can update a todo`() {
        val todoEntity = todoRepository.save(aTodo(completed = false))

        todoRepository.save(todoEntity.apply { completed = true })

        val updatedTodo = todoRepository.findByIdOrNull(todoEntity.id)
        Assertions.assertThat(updatedTodo?.completed).isTrue
    }
}
