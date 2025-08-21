package com.webforj.builtwithwebforj.todo.components;

import com.webforj.builtwithwebforj.todo.models.Todo;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optioninput.RadioButton;
import java.util.function.Consumer;

public class TodoItem extends Composite<FlexLayout> {

    private RadioButton radioButton = RadioButton.Switch();
    private Div text = new Div();
    private Div deleteButton = new Div();
    private Todo todo;
    private Consumer<Todo> onToggle;
    private Consumer<Todo> onDelete;

    /**
     * Constructs a TodoItem with a Todo model and callback functions.
     * 
     * @param todo the Todo model
     * @param onToggle callback when todo is toggled
     * @param onDelete callback when todo is deleted
     */
    public TodoItem(Todo todo, Consumer<Todo> onToggle, Consumer<Todo> onDelete) {
        this.todo = todo;
        this.onToggle = onToggle;
        this.onDelete = onDelete;
        
        // Setup component
        this.text.setText(todo.getTitle());
        
        radioButton.setChecked(todo.isCompleted());
        if (todo.isCompleted()) {
            text.setStyle("text-decoration", "line-through");
        }
        
        deleteButton.setText("✕");
        deleteButton.addClassName("todo-delete-btn");
        
        getBoundComponent().setSpacing("3px")
            .setAlignment(FlexAlignment.CENTER)
            .addClassName("item__todo--display")
            .add(radioButton, text, deleteButton);
        
        // Setup event handlers
        radioButton.onToggle(e -> {
            if (e.isToggled()) {
                text.setStyle("text-decoration", "line-through");
            } else {
                text.setStyle("text-decoration", "unset");
            }
            
            todo.setCompleted(e.isToggled());
            
            if (onToggle != null) {
                onToggle.accept(todo);
            }
        });
        
        // Delete button click handler will be set via setOnDelete()
    }

    /**
     * Backward compatibility constructor for string-based todos.
     */
    public TodoItem(String todoText) {
        this(new Todo(todoText), null, null);
    }

    /**
     * Sets the delete callback after construction.
     */
    public void setOnDelete(Consumer<Todo> onDelete) {
        this.onDelete = onDelete;
        deleteButton.onClick(e -> {
            if (this.onDelete != null) {
                this.onDelete.accept(todo);
            }
        });
    }
}