package com.example.demo.common

class TodoNotFoundException(id: Int): Exception("could not find todo with id '$id'")
