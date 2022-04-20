package com.example.demo.controller

import com.example.demo.persistence.TodoEntity
import com.example.demo.service.TodoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todo")
class TodoController(
    private val todoService: TodoService
) {
    @GetMapping("/all")
    fun getAll() =
        todoService.getAll().toResponse()

    @GetMapping
    fun search(
        @RequestParam q: String
    ) =
        todoService.search(q).toResponse()

    @GetMapping("/{id}")
    fun getOneTodo(
        @PathVariable id: Int
    ) =
        todoService.getById(id).toResponse()

    @DeleteMapping("/{id}")
    fun deleteTodo(
        @PathVariable id: Int
    ) {
        todoService.removeById(id)
    }

    @PutMapping
    fun putTodo(
        @RequestBody todoRequest: TodoRequest
    ) {
        val todo = todoRequest.id?.let {
            todoService.getById(it).apply {
                task = todoRequest.task
                completed = todoRequest.completed
            }
        } ?: todoRequest.toEntity()

        todoService.createOrUpdate(todo)
    }
}

data class TodoRequest(
    val id: Int? = null,
    val task: String,
    val completed: Boolean = false
) {
    fun toEntity() = TodoEntity(
        task = task,
        completed = completed
    )
}

data class TodoResponse(
    val id: Int,
    val task: String,
    val completed: Boolean,
)
fun TodoEntity.toResponse() =
    TodoResponse(id, task,completed)
fun List<TodoEntity>.toResponse() =
    map {it.toResponse()}
