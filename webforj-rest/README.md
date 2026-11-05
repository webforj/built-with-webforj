# REST

A webforJ and Spring Boot demo that shows two ways to load paginated data from a REST API into a table.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-rest%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-rest%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-rest%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

The app displays the same customer records in two tabs. One tab loads every record into memory and pages locally, and the other fetches only the current page from the backend. The REST endpoints are served by the same application.

## Features

- **In-memory loading**: All customers are held in memory and paged locally
- **Lazy loading**: The backend is asked for one page at a time
- **REST backend**: Endpoints for listing, paging, lookup by id and counting customers
- **Router exclusion**: The REST endpoints are kept out of the webforJ router
- **Sample data**: Customers are generated into an in-memory database at startup

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-rest
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

## Learn more

- [Loading data from REST APIs](https://docs.webforj.com/blog/data-loading-strategies-rest-apis)
- [webforJ documentation](https://docs.webforj.com)
- [Repository overview](https://docs.webforj.com/docs/advanced/repository/overview)
- [DelegatingRepository guide](https://docs.webforj.com/docs/advanced/repository/delegating-repository)
- [Spring Boot integration](https://docs.webforj.com/docs/integrations/spring/spring-boot)
- [Table component](https://docs.webforj.com/docs/components/table/overview)
- [Navigator component](https://docs.webforj.com/docs/components/navigator)
- [Spring Boot reference](https://docs.spring.io/spring-boot/reference/)
- [Building a RESTful web service](https://spring.io/guides/gs/rest-service/)
