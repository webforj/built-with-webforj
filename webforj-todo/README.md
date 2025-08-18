# webforj-todo

A webforJ application powered by Spring Boot. This project combines the power of webforJ framework with Spring Boot's comprehensive ecosystem for building enterprise applications.

## Prerequisites

- Java 21 or newer  
- Maven 3.9+

## Getting Started

To run the application in development mode:

```bash
mvn spring-boot:run
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

### Spring Boot Features

This project leverages Spring Boot's features:

- **Embedded Server**: No need to deploy WAR files, runs as a standalone JAR
- **Auto-configuration**: Spring Boot automatically configures your application
- **DevTools**: Automatic restart when code changes (included by default)
- **Spring Ecosystem**: Easy integration with Spring Data, etc.

### Hot Reload with DevTools

Spring Boot DevTools is included for automatic application restart:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <optional>true</optional>
</dependency>
```

Your application will automatically restart when files on the classpath change.


## Running Integration Tests

To run end-to-end and integration tests:

```bash
mvn verify
```

This command runs your integration tests using Spring Boot Test framework. Tests annotated with `@SpringBootTest` will:
- Start the full Spring application context
- Run on a random port to avoid conflicts
- Execute Playwright browser tests against the running application
- Automatically shut down after tests complete

## Spring Boot Configuration

Configure your application using `src/main/resources/application.properties`:

```properties
# Application name
spring.application.name=webforj-todo

# Server configuration
server.port=8080

# Add your custom configurations here
```

## Building for Production

To create an executable JAR:

```bash
mvn clean package -Pprod
java -jar target/webforj-todo-1.0-SNAPSHOT.jar
```

Or build and run a Docker image:

```bash
# Build the Docker image
mvn spring-boot:build-image

# Run the Docker container
docker run -p 8080:8080 webforj-todo:1.0-SNAPSHOT

# Or run with environment variables
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod webforj-todo:1.0-SNAPSHOT
```

## Learn More

Explore the webforJ ecosystem through our documentation and examples:

- [Full Documentation](https://docs.webforj.com)
- [Component Overview](https://docs.webforj.com/docs/components/overview)
- [Quick Tutorial](https://docs.webforj.com/docs/introduction/tutorial/overview)
- [Advanced Topics](https://docs.webforj.com/docs/advanced/overview)

### Spring Boot Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/)
