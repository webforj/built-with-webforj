package com.webforj.builtwithwebforj.todo.service;

import com.webforj.builtwithwebforj.todo.models.Todo;
import com.webforj.builtwithwebforj.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    
    @Autowired
    private TodoRepository todoRepository;
    
    public List<Todo> list() {
        return todoRepository.findAll();
    }
    
    public Todo add(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        Todo todo = new Todo(title.trim());
        return todoRepository.save(todo);
    }
    
    public Todo toggle(String id) {
        Optional<Todo> todoOpt = todoRepository.findById(id);
        if (todoOpt.isPresent()) {
            Todo todo = todoOpt.get();
            todo.toggle();
            return todoRepository.save(todo);
        }
        return null;
    }
    
    public void delete(String id) {
        todoRepository.deleteById(id);
    }
}