# webforJ Spring CRUD Application

A modern music artist management system built with webforJ and Spring Boot, demonstrating full-stack Java development with a modern web UI.

![webforJ Version](https://img.shields.io/badge/webforJ-25.02-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)
![Java](https://img.shields.io/badge/Java-21-orange)

## Overview

This app showcases webforJ's UI components integrated with Spring Boot's backend capabilities. The end result is a complete CRUD (Create, Read, Update, Delete) interface for managing music artists with a responsive design.

## Features

- **Modern UI**: Gmail-style avatars with artist initials
- **Full CRUD Operations**: Add, edit, delete, and search music artists
- **Smart Filtering**: Built-in table filtering with webforJ's `SpringDataRepository`
- **Data Validation**: Jakarta validation with user-friendly error messages
- **Responsive Design**: Clean, professional interface that works on all screen sizes
- **Auto Data Binding**: Automatic form-to-model binding with validation
- **In-Memory Database**: H2 database with sample data pre-loaded

## Tech stack

- **Frontend**: webforJ 25.02
- **Backend**: Spring Boot 3.5.3 with Spring Data JPA
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Java Version**: 21

## Getting started

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj/webforj-crud
cd webforj-crud
```

2. Run the application:
```bash
mvn spring-boot:run
```

3. Open your browser and navigate to:
```
http://localhost:8080/artists
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
src/main/java/com/webforj/crud/
  Application.java           # Main Spring Boot application
  components/
    ArtistDialog.java     # Add/Edit dialog component
    renderers/
      ArtistAvatarRenderer.java  # Custom table renderer
  config/
    DataInitializer.java  # Sample data loader
  entity/
    MusicArtist.java      # JPA entity
  repository/
    MusicArtistRepository.java  # Spring Data repository
  service/
    MusicArtistService.java     # Business logic layer
  views/
    MusicArtistsView.java       # Main UI view
```

## Learn More

- [webforJ Documentation](https://docs.webforj.com)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)