# Todo

A todo list app built with webforJ and Spring Boot, organized with the MVC pattern.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-todo%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-todo%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-todo%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

This app shows how to build a small webforJ UI from reusable components and keep the logic in a controller and a service. Users add todos, toggle them done and delete them, and filter the list by status. Todos are stored in an in-memory database and sample todos load at startup.

## Features

- **Add, toggle and delete**: Add items from a text field, mark them done with a switch and remove them
- **Filtering**: The footer filters the list by All, Active or Completed
- **MVC structure**: A controller sits between the view and the service layer
- **Reusable components**: Item, list and footer are separate components
- **Persistence**: Todos are stored in an in-memory database
- **Sample data**: Example todos are added when the database is empty

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-todo
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

### H2 database console

To view the database directly:

1. Open `http://localhost:8080/h2-console`
2. Use these connection settings:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

## Learn more

- [Building a todo app with MVC](https://docs.webforj.com/blog/webforj-mvc)
- [webforJ documentation](https://docs.webforj.com)
- [Spring Boot documentation](https://spring.io/projects/spring-boot)
