# webforJ Spring Todo Application

A modern todo list application built with webforJ and Spring Boot, demonstrating MVC architecture with a clean, responsive UI.

![webforJ Version](https://img.shields.io/badge/webforJ-25.03-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)
![Java](https://img.shields.io/badge/Java-21-orange)

## Overview

This app showcases webforJ's component-based UI integrated with Spring Boot's backend capabilities following the MVC pattern. The end result is a fully functional todo list application with filtering, persistence, and a clean user interface.

## Features

- **Modern UI**: Clean, minimalist design with intuitive controls
- **Full CRUD Operations**: Add, toggle, and delete todo items
- **Smart Filtering**: Filter todos by All, Active, or Completed status
- **Data Persistence**: H2 database with state management
- **MVC Architecture**: Clean separation of concerns with controller pattern
- **Responsive Design**: Professional interface that works on all screen sizes
- **Real-time Updates**: UI updates with state synchronization
- **Sample Data**: Pre-loaded with example todos for immediate testing

## Tech stack

- **Frontend**: webforJ 25.03
- **Backend**: Spring Boot 3.5.3 with Spring Data JPA
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Java Version**: 21

## Getting started

### Prerequisites

- Java 17 or 21
- Maven 3.6+

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-todo
```

2. Run the application:
```bash
mvn spring-boot:run
```

3. Open your browser and navigate to:
```
http://localhost:8080
```

### H2 database console

To view the database directly:
1. Navigate to `http://localhost:8080/h2-console`
2. Use these connection settings:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

## Project structure

```
src/main/java/com/webforj/builtwithwebforj/todo/
  Application.java           # Main Spring Boot application
  components/
    TodoFooter.java         # Footer with filters and count
    TodoItem.java           # Individual todo item component
    TodoList.java           # Main todo list container
  config/
    DataInitializer.java    # Sample data loader
  controller/
    TodoController.java     # MVC controller layer
  models/
    Todo.java              # JPA entity
  repository/
    TodoRepository.java     # Spring Data repository
  service/
    TodoService.java        # Business logic layer
  views/
    TodoView.java          # Main UI view
```

## Architecture

This application follows the MVC (Model-View-Controller) pattern:

- **Model**: JPA entities and Spring Data repositories handle data persistence
- **View**: webforJ components provide the user interface
- **Controller**: Spring-managed controllers coordinate between view and model

For more details on the architecture, see the [MVC Architecture Guide](MVC_ARCHITECTURE_GUIDE.md).

## Learn More

- [webforJ Documentation](https://docs.webforj.com)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)