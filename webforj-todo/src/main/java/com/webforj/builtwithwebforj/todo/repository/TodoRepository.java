package com.webforj.builtwithwebforj.todo.repository;

import com.webforj.builtwithwebforj.todo.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, String> {
}