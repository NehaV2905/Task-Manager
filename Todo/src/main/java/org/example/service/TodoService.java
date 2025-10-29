package org.example.service;

import org.example.model.Todo; // Assuming 'ToDo' is the model class
import java.util.List;

public interface TodoService { // 5 usages, 1 implementation

    List<Todo> findAll(); // 2 usages, 1 implementation

    Todo findById(Long id); // 2 usages, 1 implementation

    Todo save(Todo todo); // 2 usages, 1 implementation

    void deleteById(Long id); // 3 usages, 1 implementation

    Todo update(Long id, Todo todoDetails); // 2 usages, 1 implementation

}