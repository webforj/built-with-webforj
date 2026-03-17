# webforJ Bookstore

A book inventory management system built with webforJ and Spring Boot, demonstrating CRUD operations, role-based access control, and data filtering.

![webforJ Version](https://img.shields.io/badge/webforJ-25.11-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)
![Java](https://img.shields.io/badge/Java-21-orange)

## Overview

This app showcases webforJ's UI components integrated with Spring Boot and Spring Security. It provides a complete book inventory system with genre management, search and filtering, and role-based access to admin sections.

## Features

- **Book Inventory**: Browse, search, add, and edit books with a sortable, filterable table
- **Genre Management**: Create and delete genres with custom colors, displayed as colored chips
- **Search & Filter**: Full-text search across titles and authors, genre-based filtering
- **Role-Based Access**: Spring Security with login, logout, and admin-only views
- **Data Binding**: Automatic form-to-model binding with Jakarta validation
- **In-Memory Database**: H2 database with sample data pre-loaded via Java Faker

## Tech Stack

- **Frontend**: webforJ 25.11
- **Backend**: Spring Boot 3.5.3 with Spring Data JPA
- **Security**: Spring Security with in-memory authentication
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Java Version**: 21

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.9+

### Running the App

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-bookstore
```

2. Run the application:
```bash
mvn spring-boot:run
```

3. Open your browser and navigate to:
```
http://localhost:8080
```

### Test Credentials

- **User**: `user` / `password` (inventory access)
- **Admin**: `admin` / `admin` (inventory + management access)

## Learn More

- [webforJ Documentation](https://docs.webforj.com)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [webforJ Security Documentation](https://docs.webforj.com/docs/security/overview)
