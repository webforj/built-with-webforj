# built-with-webforj

This repository contains various webforJ projects that showcase different aspects of the webforJ framework. Each project demonstrates specific features and capabilities of webforJ, providing practical examples for developers.

## Projects

Name | Source | Version | Description
--- | --- | --- | ---
[webforj-dashboard](https://docs.webforj.com/dashboard) | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-dashboard) | `25.02` | A demonstrative cryptocurrency dashboard app using test data, that showcases webforJ's advanced `Table` and UI capabilities. Features a responsive layout with real-time data visualization using webforJ's Google Charts API, dark/light theme support, and multiple views including market overview, news feed, portfolio analytics, and settings management.
[webforj-howdy↗️](https://docs.webforj.com/howdy/you) | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-howdy) | `25.02` | A web-based demonstration project built with webforJ framework. It showcases how to create a modern web app using Java, featuring a responsive user interface, Google Charts integration, and built-in routing capabilities. This app serves as a practical example for developers learning WebforJ's core concepts and best practices.
[webforj-tictactoe↗️](https://docs.webforj.com/tictactoe/) | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-tictactoe) | `25.02` | A simple implementation of a Tic-Tac-Toe game that supports two players taking turns. The game utilizes webforJ namespaces to share the Java game object between running instances. This project demonstrates the capabilities of webforJ namespaces. No database, RESTful API, or WebSocket implementation is used in this version.
webforj-explorer | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-explorer) | `25.02` | The webforJ Explorer app is a minimal code viewer that mimics the VSCode layout. It uses webforJ's layout system, a file tree on the left, and a Monaco editor to display file contents in read-only mode. This project shows how to integrate dynamic data, build a tree UI, and embed third-party components.
webforj-crud | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-crud) | `25.02` | A simple CRUD application demonstrating the use of Spring Boot and JPA with webforJ. This music artist management system showcases how to utilize Spring Data repositories, JPA entities, and validation within a webforJ UI, featuring automatic data binding, built-in table filtering with `SpringDataRepository`, and modern components like table renderers. Perfect for developers looking to combine webforJ's frontend capabilities with Spring's robust backend ecosystem.
webforj-todo | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-todo) | `25.03` | A modern todo list application built with webforJ and Spring Boot that demonstrates MVC architecture patterns. Features a clean, responsive UI with full CRUD operations, smart filtering (All/Active/Completed), real-time state updates, and H2 database persistence. This project showcases best practices for combining webforJ's component-based frontend with Spring's backend services using a clean controller pattern.
[PingPal↗️](https://www.pingpal.dev/) | [Source](https://github.com/webforj/built-with-webforj/tree/main/pingpal) | `25.02` | **🏆 First Place Winner - Built with webforJ Contest** • A Postman-inspired API testing and development tool created by [Rick van Baalen](https://www.linkedin.com/in/rickvanbaalen/). PingPal provides an interface for testing and managing API requests, featuring authentication support, custom headers, formatted responses, and request history. This contest-winning project showcases the power of webforJ for building professional developer tools.
[startforJ↗️](https://docs.webforj.com/startforj/) | | | The startforJ app generates a minimal starter project based on various available archetypes that includes all required dependencies, configuration files, and a pre-wired layout - ready to build on.

## Getting Started

To run any of the projects in this repository:

1. Navigate to the project directory:

   ```bash
   cd webforj-howdy  # or webforj-tictactoe, webforj-explorer, pingpal
   ```

2. Run the app using Maven Jetty plugin:

   ```bash
   mvn jetty:run
   ```

3. Open your browser and navigate to the URL shown in the console (typically `http://localhost:8080`)

## Requirements

- Java 17 or higher
- Maven
