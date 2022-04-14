package com.example.demo.controller

import com.example.demo.common.TodoNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorControllerHandler {

    data class Error(
        val status: Int,
        val message: String?,
        val other: String
    )

    @ExceptionHandler(TodoNotFoundException::class)
    fun handleTodoNotFoundException(ex: TodoNotFoundException) =
        ResponseEntity(
            Error(
                status = HttpStatus.NOT_FOUND.value(),
                message = ex.message,
                other = "look ma, this is custom stuff"
            ),
            HttpStatus.NOT_FOUND
        )
}


