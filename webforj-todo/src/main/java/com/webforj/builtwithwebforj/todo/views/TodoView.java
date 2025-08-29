package com.webforj.builtwithwebforj.todo.views;

import com.webforj.builtwithwebforj.todo.components.TodoList;
import com.webforj.builtwithwebforj.todo.controller.TodoController;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.router.annotation.Route;

/**
 * Main view for the Todo application.
 * This view is responsible for creating and displaying the TodoList component
 * with proper Spring dependency injection through the controller.
 */
@Route("/")
public class TodoView extends Composite<Div> {

    /**
     * Constructs a new TodoView with the specified controller.
     * Following proper MVC pattern with controller injection.
     *
     * @param todoController the controller for managing todos
     */
    public TodoView(TodoController todoController) {

        // Create and add the TodoList component with the injected controller
        TodoList todoList = new TodoList(todoController);
        getBoundComponent().add(todoList);
    }
}
