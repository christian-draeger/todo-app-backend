package com.example.demo.service

import com.example.demo.common.TodoNotFoundException
import com.example.demo.persistence.TodoEntity
import com.example.demo.persistence.TodoRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull


@SpringBootTest
class TodoServiceImplTest(
    @Autowired private val todoService: TodoService
) {

    @MockkBean
    private lateinit var todoRepository: TodoRepository

    private fun aTodo(
        task: String = "a sample task",
        completed: Boolean = false
    ) = TodoEntity(
        task = task,
        completed = completed
    )

    @Test
    fun `will create or update by calling repository save`() {
        val aTestTodo = aTodo()
        every { todoRepository.save(aTestTodo) } returns aTestTodo

        todoService.createOrUpdate(aTestTodo)
        verify { todoRepository.save(aTestTodo) }
    }

    @Test
    fun `can get existing todo by id`() {
        val aTestTodo = aTodo()
        every { todoRepository.findByIdOrNull(4711) } returns aTestTodo

        val result = todoService.getById(4711)
        assertThat(result).isEqualTo(aTestTodo)
    }

    @Test
    fun `will throw if trying to get todo by id that not exists`() {
        val aTestTodo = aTodo()
        every { todoRepository.findByIdOrNull(4711) } returns null

        Assertions.assertThrows(TodoNotFoundException::class.java) {
            todoService.getById(4711)
        }
    }

    @Test
    fun `can find all completed`() {
        val anCompletedTodo = aTodo(completed = true)
        every { todoRepository.findAllByCompleted(true) } returns listOf(anCompletedTodo)

        val result = todoService.getByCompleted(true)
        assertThat(result).containsExactly(anCompletedTodo)
    }

    @Test
    fun `can remove all completed`() {
        justRun { todoRepository.deleteAllByCompleted(any()) }

        todoService.removeAllCompleted()
        verify { todoRepository.deleteAllByCompleted(true) }
    }

    @Test
    fun `can get all todos`() {
        val aTodo = aTodo()
        every { todoRepository.findAll() } returns listOf(aTodo)

        val result = todoService.getAll()
        assertThat(result).containsExactly(aTodo)
    }

    @Test
    fun `can search todos by partial task`() {
        val aTodo = aTodo()
        every { todoRepository.findAllByTaskContainsIgnoreCase(any()) } returns listOf(aTodo)

        val result = todoService.search("something")
        assertThat(result).containsExactly(aTodo)
    }
}
