package com.example.demo.service

import com.example.demo.common.TodoNotFoundException
import com.example.demo.persistence.TodoEntity
import com.example.demo.persistence.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl(
    private val todoRepository: TodoRepository
): TodoService {
    override fun createOrUpdate(todo: TodoEntity) {
        todoRepository.save(todo)
    }

    override fun removeById(id: Int) {
        todoRepository.findByIdOrNull(id)?.run {
            todoRepository.delete(this)
        } ?: throw TodoNotFoundException(id)

    }
    override fun getById(id: Int): TodoEntity {
        return todoRepository.findByIdOrNull(id)
            ?: throw TodoNotFoundException(id)
    }

    override fun removeAllCompleted() {
        todoRepository.deleteAllByCompleted(true)
    }

    override fun getByCompleted(completed: Boolean): List<TodoEntity> {
        return todoRepository.findAllByCompleted(completed)
    }

    override fun getAll(): List<TodoEntity> {
        return todoRepository.findAll()
    }

    override fun search(partialTask: String): List<TodoEntity> {
        return todoRepository.findAllByTaskContainsIgnoreCase(partialTask)
    }
}
