package com.example.demo.controller

import com.example.demo.persistence.TodoEntity
import com.example.demo.service.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Date

@RestController
@RequestMapping("/todo")
class TodoController(
    private val todoService: TodoService
) {
    @GetMapping("/all")
    fun getAll() = ResponseEntity
        .ok()
        .body(todoService.getAll().toResponse())

    @GetMapping
    fun search(
        @RequestParam q: String
    ): ResponseEntity<List<TodoResponse>> = when (q) {
        "magic" -> ResponseEntity.status(418).build()
        else -> ResponseEntity.ok().body(todoService.search(q).toResponse())
    }

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
    ): ResponseEntity<Unit> {
        val todo = todoRequest.id?.let {
            todoService.getById(it).apply {
                task = todoRequest.task
                completed = todoRequest.completed
            }
        } ?: todoRequest.toEntity()

        todoService.createOrUpdate(todo)
        return ResponseEntity.status(201).build()
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
    val created: Date,
    val lastUpdated: Date
)

fun TodoEntity.toResponse() = TodoResponse(id, task,completed, createdDate, modifiedDate)
fun List<TodoEntity>.toResponse() = map { it.toResponse() }
