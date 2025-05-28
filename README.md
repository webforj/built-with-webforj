# built-with-webforj

This repository contains various webforJ projects that showcase different aspects of the webforJ framework. Each project demonstrates specific features and capabilities of webforJ, providing practical examples for developers.

## Projects

Name | Source | Version | Description
--- | --- | --- | ---
webforj-howdy | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-howdy) | `25.00` | A web-based demonstration project built with webforJ framework. It showcases how to create a modern web app using Java, featuring a responsive user interface, Google Charts integration, and built-in routing capabilities. This app serves as a practical example for developers learning WebforJ's core concepts and best practices.
webforj-tictactoe | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-tictactoe) | `25.00` | A simple implementation of a Tic-Tac-Toe game that supports two players taking turns. The game utilizes webforJ namespaces to share the Java game object between running instances. This project demonstrates the capabilities of webforJ namespaces. No database, RESTful API, or WebSocket implementation is used in this version.
webforj-explorer | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-explorer) | `25.00` | The webforJ Explorer app is a minimal code viewer that mimics the VSCode layout. It uses webforJ's layout system, a file tree on the left, and a Monaco editor to display file contents in read-only mode. This project shows how to integrate dynamic data, build a tree UI, and embed third-party components.
[startforJ↗️](https://docs.webforj.com/startforj/) | | | The startforJ app generates a minimal starter project based on various available archetypes that includes all required dependencies, configuration files, and a pre-wired layout - ready to build on.

## Getting Started

To run any of the projects in this repository:

1. Navigate to the project directory:

   ```bash
   cd webforj-howdy  # or webforj-tictactoe, webforj-explorer
   ```

2. Run the app using Maven Jetty plugin:

   ```bash
   mvn jetty:run
   ```

3. Open your browser and navigate to the URL shown in the console (typically `http://localhost:8080`)

## Requirements

- Java 17 or higher
- Maven