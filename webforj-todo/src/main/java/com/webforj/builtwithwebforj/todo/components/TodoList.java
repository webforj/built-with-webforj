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
    private FlexLayout todoItemsContainer;
    private TodoFooter todoFooter;
    private H1 title = new H1("Todos");
    private TodoController.FilterType currentFilter = TodoController.FilterType.ALL;

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
        
        // Create container for todo items
        todoItemsContainer = FlexLayout.create()
            .vertical()
            .build()
            .setSpacing("5px");
        
        // Create footer with filter change handler
        todoFooter = new TodoFooter();
        todoFooter.setOnFilterChange(this::handleFilterChange);
        
        // Create main display with text field and items container (without footer)
        todoDisplay = FlexLayout.create(text, todoItemsContainer)
            .vertical()
            .build()
            .setSpacing("5px")
            .addClassName("todo--display");
        
        // Create wrapper for display and footer (footer outside the padded area)
        Div todoWrapper = new Div();
        todoWrapper.addClassName("todo-wrapper");
        todoWrapper.add(todoDisplay, todoFooter);
        
        getBoundComponent().add(title, todoWrapper);
        
        // Setup event handlers
        text.onKeypress(e -> {
            if (e.getKeyCode().equals(KeypressEvent.Key.ENTER) && !text.getText().isBlank()) {
                Todo todo = todoController.addNewTodo(text.getText());
                if (todo != null) {
                    text.setText("");
                    refreshTodoDisplay();
                }
            }
        });
        
        // Load existing todos
        refreshTodoDisplay();
    }

    /**
     * Creates a todo item UI component for display.
     */
    private TodoItem createTodoItem(Todo todo) {
        TodoItem item = new TodoItem(todo, this::handleTodoToggle, null);
        item.setOnDelete(this::handleTodoDelete);
        return item;
    }

    /**
     * Handles when a todo is toggled.
     */
    private void handleTodoToggle(Todo todo) {
        todoController.toggleTodo(todo.getId());
        refreshTodoDisplay();
    }

    /**
     * Handles when a todo is deleted.
     */
    private void handleTodoDelete(Todo todo) {
        todoController.removeTodo(todo.getId());
        refreshTodoDisplay();
    }
    
    /**
     * Updates the footer with current todo count.
     */
    private void updateFooter() {
        todoFooter.updateItemCount(todoController.getTodoCount());
    }
    
    /**
     * Handles filter change events from the footer.
     * 
     * @param filterType the new filter type
     */
    private void handleFilterChange(TodoFooter.FilterType filterType) {
        // Convert TodoFooter.FilterType to TodoController.FilterType
        switch (filterType) {
            case ALL:
                currentFilter = TodoController.FilterType.ALL;
                break;
            case ACTIVE:
                currentFilter = TodoController.FilterType.ACTIVE;
                break;
            case COMPLETED:
                currentFilter = TodoController.FilterType.COMPLETED;
                break;
        }
        refreshTodoDisplay();
    }
    
    /**
     * Refreshes the todo display based on current filter.
     */
    private void refreshTodoDisplay() {
        // Clear current display
        todoItemsContainer.removeAll();
        
        // Get filtered todos from controller
        List<Todo> todos = todoController.getFilteredTodos(currentFilter);
        
        // Add todos to display
        for (Todo todo : todos) {
            TodoItem item = createTodoItem(todo);
            todoItemsContainer.add(item);
        }
        
        // Update footer count
        updateFooter();
    }
}