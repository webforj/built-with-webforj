package com.webforj.builtwithwebforj.todo.controller;

import com.webforj.builtwithwebforj.todo.models.Todo;
import com.webforj.builtwithwebforj.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TodoController {
    
    @Autowired
    private TodoService todoService;
    
    public List<Todo> getAllTodos() {
        return todoService.list();
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
}