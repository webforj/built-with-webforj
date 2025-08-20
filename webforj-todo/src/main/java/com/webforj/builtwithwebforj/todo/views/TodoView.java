package com.webforj.builtwithwebforj.todo.views;

import com.webforj.builtwithwebforj.todo.components.TodoList;
import com.webforj.builtwithwebforj.todo.service.TodoService;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.router.annotation.Route;

/**
 * Main view for the Todo application.
 * This view is responsible for creating and displaying the TodoList component
 * with proper Spring dependency injection.
 */
@Route("/")
public class TodoView extends Composite<Div> {

    private final TodoService todoService;

    /**
     * Constructs a new TodoView with the specified service.
     * Following the same dependency injection pattern as MusicArtistsView.
     * 
     * @param todoService the service for managing todos
     */
    public TodoView(TodoService todoService) {
        this.todoService = todoService;
        
        // Create and add the TodoList component with the injected service
        TodoList todoList = new TodoList(todoService);
        getBoundComponent().add(todoList);
    }
}