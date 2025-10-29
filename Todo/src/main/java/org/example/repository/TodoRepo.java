package org.example.repository;

import org.example.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TodoRepo extends JpaRepository<Todo, Long> {
}
