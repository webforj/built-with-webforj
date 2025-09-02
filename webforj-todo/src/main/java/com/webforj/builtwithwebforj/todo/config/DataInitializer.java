package com.webforj.builtwithwebforj.todo.config;

import com.webforj.builtwithwebforj.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data initialization class that adds sample todos to the database
 * when the application starts (only if the database is empty).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TodoService todoService;

    @Override
    public void run(String... args) throws Exception {
        // Only add sample data if the database is empty
        long currentCount = todoService.list().size();

        if (currentCount == 0) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        // Create initial todos - same as in the demo
        todoService.add("Go get groceries");
        todoService.add("Water Plants");
        todoService.add("Gym - upper body day");
        todoService.add("Call mom");
        todoService.add("Finish programming my webforJ app!");
    }
}