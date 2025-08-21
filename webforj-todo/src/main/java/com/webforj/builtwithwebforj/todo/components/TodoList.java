package com.webforj.builtwithwebforj.todo.components;

import com.webforj.builtwithwebforj.todo.models.Todo;
import com.webforj.builtwithwebforj.todo.controller.TodoController;
import com.webforj.component.Composite;
import com.webforj.component.Expanse;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.layout.flexlayout.FlexLayout;
import java.util.List;

public class TodoList extends Composite<Div> {

    private final TodoController todoController;
    private TextField text = new TextField();
    private FlexLayout todoDisplay;
    private H1 title = new H1("Todos");

    /**
     * Constructs a new TodoList with the specified controller.
     * 
     * @param todoController the controller for managing todos
     */
    public TodoList(TodoController todoController) {
        this.todoController = todoController;
        
        // Setup components
        getBoundComponent().addClassName("frame");
        
        text.setExpanse(Expanse.XLARGE);
        text.setPlaceholder("Add Todo item. Press Enter to save.");
        
        todoDisplay = FlexLayout.create(text)
            .vertical()
            .build()
            .setSpacing("5px")
            .addClassName("todo--display");
        
        getBoundComponent().add(title, todoDisplay);
        
        // Setup event handlers
        text.onKeypress(e -> {
            if (e.getKeyCode().equals(KeypressEvent.Key.ENTER) && !text.getText().isBlank()) {
                Todo todo = todoController.addNewTodo(text.getText());
                if (todo != null) {
                    addTodoItem(todo);
                    text.setText("");
                }
            }
        });
        
        // Load existing todos
        List<Todo> todos = todoController.getAllTodos();
        for (Todo todo : todos) {
            addTodoItem(todo);
        }
    }

    /**
     * Adds a todo item to the UI display.
     */
    private void addTodoItem(Todo todo) {
        TodoItem item = new TodoItem(todo, this::handleTodoToggle, null);
        item.setOnDelete(t -> {
            handleTodoDelete(t);
            todoDisplay.remove(item);
        });
        todoDisplay.add(item);
    }

    /**
     * Handles when a todo is toggled.
     */
    private void handleTodoToggle(Todo todo) {
        todoController.toggleTodo(todo.getId());
    }

    /**
     * Handles when a todo is deleted.
     */
    private void handleTodoDelete(Todo todo) {
        todoController.removeTodo(todo.getId());
        // Item will be removed from display by the caller
    }
}