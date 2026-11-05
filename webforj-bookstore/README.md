# webforJ Bookstore

A book inventory demo built with webforJ and Spring Boot.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-bookstore%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-bookstore%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-bookstore%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

Users sign in, browse a sortable table of books, search by text, filter by genre and open a drawer to view or edit a book. Genres are managed from a separate drawer, and an administration page is limited to the admin role. It shows tables, data binding with validation, database access and role-based access control.

## Features

- **Book inventory**: A sortable table of books with row selection. Clicking a row opens a drawer to view or edit the book
- **Search and genre filter**: Narrow the table to matching titles and authors, or to a single genre
- **Genre management**: Create and delete genres with custom colors, shown as colored chips
- **Data binding**: The book form is bound to the book model and validated
- **Role-based access**: Login, logout and access denied pages, with an admin-only management page
- **In-memory database**: Sample genres, publishers, authors and books are generated at startup

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-bookstore
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

### Test logins

- **User**: `user` / `password` (inventory access)
- **Admin**: `admin` / `admin` (inventory and management access)

### Database console

The H2 console is available at `http://localhost:8080/h2-console`. Use the JDBC URL `jdbc:h2:mem:bookstore`, user `sa` and an empty password.

## Learn more

- [Building a full-featured demo with Spring Boot](https://docs.webforj.com/blog/webforj-bookstore)
- [webforJ documentation](https://docs.webforj.com)
- [Table component](https://docs.webforj.com/docs/components/table)
- [Data binding](https://docs.webforj.com/docs/data-binding/overview)
- [Route security](https://docs.webforj.com/docs/security/overview)
- [Spring Security reference](https://docs.spring.io/spring-security/reference/)
