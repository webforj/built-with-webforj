# webforJ `SideMenu` Archetype

A minimal and ready-to-use starting point for building webforJ applications. This archetype includes the essential setup to help you launch your project quickly and focus on your app logic.

## Prerequisites

- Java 21 or newer  
- Maven 3.9+

## Getting Started

To run the application in development mode:

```bash
mvn jetty:run
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

This project is preconfigured to use the **Jetty Maven Plugin**, which makes development fast and iterative. It includes automatic scanning for class and resource changes.

### Jetty Auto-Reload (Hot Deployment)

By default, this project enables **Jetty's scan mode** using the following property:

```xml
<jetty.scan>1</jetty.scan>
```

This means Jetty will **poll for changes in compiled classes and resources every second**, allowing the app to **auto-reload** without restarting the server. This is great for quick feedback while developing UI or backend logic.

If you're using a live reload tool (like JRebel or similar), you may want to set this to `0` to disable it.

```xml
<jetty.scan>0</jetty.scan>
```

## Running Integration Tests

To run end-to-end and integration tests:

```bash
mvn verify
```

This command:
- Starts Jetty before tests using the `jetty:start` goal.
- Runs integration tests using the **Failsafe Plugin** (tests ending with `*IT.java`).
- Shuts down Jetty after tests complete.

## Learn More

Explore the webforJ ecosystem through our documentation and examples:

- [Full Documentation](https://docs.webforj.com)
- [Component Overview](https://docs.webforj.com/docs/components/overview)
- [Quick Tutorial](https://docs.webforj.com/docs/introduction/tutorial/overview)
- [Advanced Topics](https://docs.webforj.com/docs/advanced/overview)