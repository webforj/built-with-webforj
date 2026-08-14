# built-with-webforj

This repository contains various webforJ projects that showcase different aspects of the webforJ framework. Each project demonstrates specific features and capabilities of webforJ, providing practical examples for developers.

## Projects

Name | Source | Version | Description
--- | --- | --- | ---
[webforj-dashboard](https://docs.webforj.com/dashboard) | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-dashboard) | `26.01` | A demonstrative cryptocurrency dashboard app using test data, that showcases webforJ's advanced `Table` and UI capabilities. Features a responsive layout with real-time data visualization using webforJ's Google Charts API, dark/light theme support, and multiple views including market overview, news feed, portfolio analytics, and settings management.
[webforj-howdy↗️](https://docs.webforj.com/howdy/you) | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-howdy) | `26.02-SNAPSHOT` | A web-based demonstration project built with webforJ framework. It showcases how to create a modern web app using Java, featuring a responsive user interface, Google Charts integration, and built-in routing capabilities. This app serves as a practical example for developers learning webforJ's core concepts and best practices.
[webforj-tictactoe↗️](https://docs.webforj.com/tictactoe/) | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-tictactoe) | `26.02-SNAPSHOT` | A simple implementation of a Tic-Tac-Toe game that supports two players taking turns. The game utilizes webforJ namespaces to share the Java game object between running instances. This project demonstrates the capabilities of webforJ namespaces. No database, RESTful API, or WebSocket implementation is used in this version.
webforj-explorer | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-explorer) | `26.02-SNAPSHOT` | The webforJ Explorer app is a minimal code viewer that mimics the VSCode layout. It uses webforJ's layout system, a file tree on the left, and a Monaco editor to display file contents in read-only mode. This project shows how to integrate dynamic data, build a tree UI, and embed third-party components.
webforj-crud | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-crud) | `26.02-SNAPSHOT` | A simple CRUD application demonstrating the use of Spring Boot and JPA with webforJ. This music artist management system showcases how to utilize Spring Data repositories, JPA entities, and validation within a webforJ UI, featuring automatic data binding, built-in table filtering with `SpringDataRepository`, and the framework's first-class table renderers — `AvatarRenderer` composed with a `CompositeRenderer` power the Artist column, replacing the previously hand-rolled avatar HTML. Perfect for developers looking to combine webforJ's frontend capabilities with Spring's robust backend ecosystem.
webforj-todo | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-todo) | `26.02-SNAPSHOT` | A modern todo list application built with webforJ and Spring Boot that demonstrates MVC architecture patterns. Features a clean, responsive UI with full CRUD operations, smart filtering (All/Active/Completed), real-time state updates, and H2 database persistence. This project showcases best practices for combining webforJ's component-based frontend with Spring's backend services using a clean controller pattern.
webforj-rest | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-rest) | `26.02-SNAPSHOT` | A Spring Boot-powered webforJ application demonstrating two approaches for handling paginated REST API data. Features side-by-side comparison of `CollectionRepository` (in-memory pagination) and `DelegatingRepository` (lazy-loading pagination) patterns with a customer management interface. Showcases webforJ's `Table` component with page-based `Navigator` layout, REST API integration, and randomly generated test data. Perfect for learning how to choose between eager and lazy data loading strategies in webforJ applications.
webforj-ghostai | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-ghostai) | `26.02-SNAPSHOT` | An AI chat demo built with webforJ and Spring AI. Features streaming markdown rendering with the `MarkdownViewer` component, predictive text input, chat memory, and MCP integration with the webforJ MCP Server for real-time documentation queries. Showcases how to build ChatGPT-style interfaces in pure Java using Google Gemini via `spring-ai-starter-model-google-genai`. **Runtime prerequisite:** set `SPRING_AI_MODEL_GOOGLE_GENAI_PROJECT_ID` and matching Google Cloud ADC credentials, otherwise Spring context init fails on the `googleGenAiChatModel` bean.
webforj-bookstore | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-bookstore) | `26.02-SNAPSHOT` | A book inventory management system built with webforJ and Spring Boot, demonstrating CRUD operations, role-based access control with Spring Security, and data filtering. Features genre management with colored chips, full-text search, sortable tables with custom renderers, and admin-only views. Uses H2 in-memory database with sample data generated via Java Faker. Test credentials for the signin card: `admin/admin` or `user/password`.
webforj-databinding | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-databinding) | `26.02-SNAPSHOT` | An employee onboarding form demonstrating webforJ data binding over nested Java beans, packaged as a traditional WAR and run on the Jetty Maven plugin. A single `BindingContext` created with automatic binding (`BindingContext.of`) wires every field, reaching nested `Address` and `EmergencyContact` beans through `@UseProperty` dotted property paths like `address.street`. Jakarta Validation cascades through the nested beans via `@Valid`, and saving opens a dialog presenting the record in a single-open `Accordion`.
webforj-focustracker | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-focustracker) | `26.02-SNAPSHOT` | A focus timer demonstrating Progressive Web App capabilities in webforJ. Features installable app support via `@AppProfile`, desktop notifications with `DesktopNotification.show()`, and dynamic app-icon badges via `App.setBadge()`. Shows how Java developers can access modern web platform features traditionally limited to JavaScript.
webforj-location-tracker | [Source](https://github.com/webforj/built-with-webforj/tree/main/webforj-location-tracker) | `26.02-SNAPSHOT` | A globe-spanning "where are my friends" demo that pairs the browser Geolocation API with **all three** faces of the webforJ Badge feature in one screen: the in-app `dwc-badge` slotted into the header bell, the browser-tab favicon overlay via `Page.setIconBadge()`, and the installable-app icon badge via `App.setBadge()`. Each friend card carries a stable Wikimedia Commons photo of their city (with signature CSS gradients as a graceful fallback), the grid sorts nearest-first using great-circle distance from the user's geolocated position, and a dark/light theme toggle sits on the far right of the toolbar. Adding a friend fires all three badges at once.
[PingPal↗️](https://www.pingpal.dev/) | [Source](https://github.com/webforj/built-with-webforj/tree/main/pingpal) | `26.02-SNAPSHOT` | **🏆 First Place Winner - Built with webforJ Contest** • A Postman-inspired API testing and development tool created by [Rick van Baalen](https://www.linkedin.com/in/rickvanbaalen/). PingPal provides an interface for testing and managing API requests, featuring authentication support, custom headers, formatted responses, and request history. This contest-winning project showcases the power of webforJ for building professional developer tools. **Runs on `http://localhost:9965`**, and needs `PINGPAL_URL` (plus `PINGPAL_USERNAME` / `PINGPAL_PASSWORD` if the target API is authed) in the environment.
[startforJ↗️](https://docs.webforj.com/startforj/) | | | The startforJ app generates a minimal starter project based on various available archetypes that includes all required dependencies, configuration files, and a pre-wired layout - ready to build on.

## Getting Started

Every project builds with Maven and runs on `http://localhost:8080` (except **pingpal**, which uses `9965`).

1. Navigate to the project directory:

   ```bash
   cd webforj-howdy   # or any other project folder
   ```

2. Start the app. Most projects have a default Maven goal wired up, so:

   ```bash
   mvn
   ```

   is enough. If a project doesn't start a server that way, use one of:

   - `mvn jetty:run` — for the classic Jetty WAR projects (webforj-dashboard, webforj-howdy, webforj-tictactoe, webforj-explorer, webforj-databinding, pingpal)
   - `mvn spring-boot:run` — for the Spring Boot projects (webforj-bookstore, webforj-crud, webforj-todo, webforj-rest, webforj-ghostai, webforj-focustracker)

3. Open your browser at the URL in the console output.

## Requirements

- **Java 21** or newer (webforj-tictactoe still targets Java 17 for legacy reasons; everything else is on 21)
- **Maven 3.9** or newer
- The webforJ `26.02-SNAPSHOT` line resolves from the Sonatype Central Portal snapshots repo, which every project's `pom.xml` already declares — no extra Maven settings are needed.
