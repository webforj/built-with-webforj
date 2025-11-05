# webforj-rest

A webforJ application powered by Spring Boot that shows two approaches for handling paginated data from REST APIs. You'll see how to load all data into memory, and how to fetch data on-demand.

## Overview

This application demonstrates two repository patterns for working with REST APIs in webforJ:

1. **CollectionRepository** - Load all data into memory once, then paginate locally
2. **DelegatingRepository** - Fetch only the data needed for the current page from the backend

## Prerequisites

- Java 17 or 21
- Maven 3.9+
- webforJ version 25.03 or newer

## Getting Started

To run the application in development mode:

```bash
mvn
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

## What You'll Learn

This application demonstrates the same customer data (100 records) displayed in two tabs, each using a different approach:

### Tab 1: CollectionRepository (In Memory)

Fetches all 100 customers from the backend at once, stores them in memory using a [`CollectionRepository`](https://docs.webforj.com/docs/advanced/repository/overview#collection-repository), then provides client-side pagination with a [`Navigator`](https://docs.webforj.com/docs/components/navigator).

**How it works:**
```java
// 1. Fetch all data from REST API
List<Customer> customers = customerService.getAllCustomers();

// 2. Create CollectionRepository from the list
CollectionRepository<Customer> repository = new CollectionRepository<>(customers);

// 3. Wire up Table and Navigator
customerTable.setRepository(repository);
navigator = new Navigator(repository, 15);
```

**When to use:**
- Small to medium datasets
- Data changes infrequently
- You need all data for client-side operations (filtering, sorting)
- Simple setup is a priority

### Tab 2: DelegatingRepository (Lazy Loading)

Only fetches 15 customers at a time from the backend as you navigate pages using a [`DelegatingRepository`](https://docs.webforj.com/docs/advanced/repository/delegating-repository).

**How it works:**
```java
// Create a DelegatingRepository with three functions
public class CustomerDelegatingRepository extends DelegatingRepository<Customer, Object> {
    public CustomerDelegatingRepository(RestClientService service) {
        super(
            // Find: Fetch page of data
            criteria -> service.fetchCustomers(criteria.getLimit(), criteria.getOffset()).stream(),

            // Count: Get total count
            criteria -> service.getCustomerCount(),

            // Find by key: Fetch single item
            key -> service.fetchCustomerById((Long) key)
        );
    }
}
```

**When to use:**
- Large datasets
- Data changes frequently
- Reduce initial load time and memory usage
- Backend supports pagination

### Backend Implementation

The Spring Boot backend provides paginated endpoints:

```java
@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
    @GetMapping
    public List<Customer> getAllCustomers() { ... }

    @GetMapping("/paginated")
    public List<Customer> getCustomersPaginated(
        @RequestParam int limit,
        @RequestParam int offset) { ... }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) { ... }

    @GetMapping("/count")
    public long getCustomerCount() { ... }
}
```

## Key Technologies

**webforJ Resources:**
- [Repository Overview](https://docs.webforj.com/docs/advanced/repository/overview) - Understanding repositories in webforJ
- [DelegatingRepository Guide](https://docs.webforj.com/docs/advanced/repository/delegating-repository) - Deep dive into lazy loading
- [Spring Boot Integration](https://docs.webforj.com/docs/integrations/spring/spring-boot) - Using webforJ with Spring Boot
- [Table Component](https://docs.webforj.com/docs/components/table/overview) - Working with tables
- [Navigator Component](https://docs.webforj.com/docs/components/navigator) - Adding pagination

**Spring Boot Resources:**
- [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Building REST APIs](https://spring.io/guides/gs/rest-service/)
