# webforj-nestedbeans

A small webforJ 26.01 demo of **data binding over nested Java beans**, packaged as a traditional WAR and run on the Jetty Maven plugin.

The form binds an `Employee` that contains a nested `Address` and `EmergencyContact`. A **single `BindingContext<Employee>`** wires every UI field, including nested fields via dotted property paths (e.g. `"address.street"`). Jakarta Validation cascades through the nested beans via `@Valid`, and `@Embeddable`/`@Embedded` mark the nested types so future JPA persistence is a drop-in change.

## Prerequisites

- Java 21 or newer
- Maven 3.9+

## Running

```bash
mvn jetty:run
```

Then open <http://localhost:8080>.

The Jetty plugin scans for class and resource changes every second (`<jetty.scan>1</jetty.scan>` in `pom.xml`), so edits redeploy automatically.

## Building a WAR

```bash
mvn clean package
```

Produces `target/webforj-nestedbeans.war`, ready to drop into any Servlet 6 container.

## Project layout

```
src/main/
├── java/com/webforj/nestedbeans/
│   ├── Application.java             # webforJ entry (@Routify, @AppTheme, @StyleSheet)
│   ├── domain/
│   │   ├── Employee.java            # @Embedded address + emergencyContact
│   │   ├── Address.java             # @Embeddable
│   │   └── EmergencyContact.java    # @Embeddable
│   ├── service/
│   │   └── EmployeeService.java     # static singleton, in-memory ArrayList
│   └── views/
│       └── EmployeeFormView.java    # single BindingContext + dot-notation
├── resources/
│   └── webforj.conf                 # webforJ entry / debug flags (HOCON)
└── webapp/
    └── WEB-INF/web.xml              # WebforjServlet mapped to /*
```

## How the binding works

```java
context = new BindingContext<>(Employee.class, true);
context.bind(firstName, "firstName").add();
context.bind(street,    "address.street").add();
context.bind(phone,     "emergencyContact.phone").add();
context.read(employee);   // reads through nested gets
context.write(employee);  // creates missing nested beans via no-arg ctor, then writes
```

One context, dotted paths for every nested field. Jakarta validation messages declared on `Address`/`EmergencyContact` light up automatically because the `Employee` fields are annotated `@Valid`.

## Viewing saved entries

The page has a **View saved** button that opens a `Dialog` containing an `Accordion`. Each accordion panel expands to show one saved employee's identity, address, and emergency contact.

## Learn more

- [Full Documentation](https://docs.webforj.com)
- [Data binding docs](https://docs.webforj.com/docs/data-binding/bindings) (nested-property pattern lands in 26.01)
- [Maven Jetty plugin](https://docs.webforj.com/docs/configuration/deploy-reload/maven-jetty-plugin)
