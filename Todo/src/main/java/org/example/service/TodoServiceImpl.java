package org.example.service;

// Imports (Partial)
import org.example.exception.TodoNotFoundException; // Assuming this is the custom exception
import org.example.model.Todo;
import org.example.repository.TodoRepo; // Assuming TodoRep is the repository interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; // Implied by .orElseThrow() usage

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepo todoRep; // Repository dependency

    // --- Read Operations ---

    @Override
    public List<Todo> findAll() {
        return todoRep.findAll();
    }

    @Override
    public Todo findById(Long id) {
        // Uses Optional<ToDo> from JpaRepository's findById, handling the not found case
        return todoRep.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("ToDo with ID " + id + " not found."));
    }

    @Override
    public Todo save(Todo todo) {
        return todoRep.save(todo);
    }

    @Override
    public Todo update(Long id, Todo todoDetails) {

        Todo existingTodo = todoRep.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("ToDo with ID " + id + " not found for update."));


        existingTodo.setTask(todoDetails.getTask());

        return todoRep.save(existingTodo);
    }

    // --- Delete Operation ---

    @Override
    public void deleteById(Long id) {
        todoRep.deleteById(id);
        // Note: A robust implementation often checks if the ID exists before deleting or relies on repository exceptions.
    }


}