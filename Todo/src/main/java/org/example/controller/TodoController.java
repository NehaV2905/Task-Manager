package org.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.model.Todo;
import org.example.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @GetMapping("/getalltodos") // or maybe just @GetMapping
    public ResponseEntity<List<Todo>> findAll() {
        logger.info("Fetching all tasks.");
        return new ResponseEntity<>(todoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        logger.info("Fetching task with ID: {}", id);
        return new ResponseEntity<>(todoService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        logger.info("Creating new task: {}", todo.getTask());
        return new ResponseEntity<>(todoService.save(todo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@RequestBody Todo todo, @PathVariable Long id) {
        logger.info("Attempting to update task with ID: {}", id);
        return new ResponseEntity<>(todoService.update(id, todo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        logger.info("Deleting task with ID: {}", id);
        todoService.deleteById(id);
    }
}