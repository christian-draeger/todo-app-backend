package com.example.demo.controller

import com.example.demo.persistence.TodoEntity
import com.example.demo.persistence.TodoRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put

@AutoConfigureMockMvc
@SpringBootTest
class TodoControllerTest(
    @Autowired private val mockMvc: MockMvc,
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

    @BeforeEach
    fun clearDatabase() {
        todoRepository.deleteAll()
    }

    @Test
    fun `can get a todo by id`() {
        val persistedTestData = todoRepository.saveAll(testData)

        mockMvc.get("/todo/${persistedTestData[1].id}")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("task", Matchers.containsString("cooking a meal"))
                }
            }
    }

    @Test
    fun `can get all todo`() {
        todoRepository.saveAll(testData)

        val responseBody: List<TodoResponse> = mockMvc.get("/todo/all")
            .andExpect {
                status { isOk() }
            }.andReturn().response.let {
                jacksonObjectMapper().readValue(it.contentAsString)
            }

        Assertions.assertThat(responseBody.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `search for todo by partial task`() {
        todoRepository.saveAll(testData)

        mockMvc.get("/todo?q=CO")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.length()", Matchers.`is`(2))
                }
            }
    }

    @Test
    fun `can remove todo`() {
        val persistedTestData = todoRepository.saveAll(testData)

        mockMvc.delete("/todo/${persistedTestData.last().id}")
            .andExpect {
                status { isOk() }
            }

        Assertions.assertThat(todoRepository.findAll().map { it.task })
            .containsExactly(
                persistedTestData[0].task,
                persistedTestData[1].task,
            )
    }

    @Test
    fun `can put todo`() {
        mockMvc.put("/todo") {
            contentType = MediaType.APPLICATION_JSON
            content = TodoRequest(task = "test").asJsonString()
        }.andExpect {
            status { isOk() }
        }
    }

    private fun TodoRequest.asJsonString() =
        jacksonObjectMapper().writeValueAsString(this)
}
