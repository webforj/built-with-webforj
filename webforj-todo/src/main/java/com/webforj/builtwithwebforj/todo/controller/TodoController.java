package com.webforj.builtwithwebforj.todo.controller;

import com.webforj.builtwithwebforj.todo.models.Todo;
import com.webforj.builtwithwebforj.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoController {
    
    @Autowired
    private TodoService todoService;
    
    public List<Todo> getAllTodos() {
        return todoService.list();
    }
    
    public List<Todo> getFilteredTodos(FilterType filterType) {
        List<Todo> allTodos = todoService.list();
        switch (filterType) {
            case ACTIVE:
                return allTodos.stream()
                    .filter(todo -> !todo.isCompleted())
                    .collect(Collectors.toList());
            case COMPLETED:
                return allTodos.stream()
                    .filter(Todo::isCompleted)
                    .collect(Collectors.toList());
            case ALL:
            default:
                return allTodos;
        }
    }
    
    public Todo addNewTodo(String title) {
        return todoService.add(title);
    }
    
    public Todo toggleTodo(String id) {
        return todoService.toggle(id);
    }
    
    public void removeTodo(String id) {
        todoService.delete(id);
    }
    
    public int getTodoCount() {
        return todoService.list().size();
    }
    
    public enum FilterType {
        ALL, ACTIVE, COMPLETED
    }
}