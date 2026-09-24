# webforj-databinding

A small webforJ 26.01 demo of **data binding over nested Java beans**, packaged as a traditional WAR and run on the Jetty Maven plugin.

The form binds an `Employee` that contains a nested `Address` and `EmergencyContact`. A **single `BindingContext<Employee>`** created with `BindingContext.of()` (automatic binding) wires every UI field: fields whose names match `Employee` properties bind by name, and nested fields are mapped with `@UseProperty` dotted property paths (e.g. `"address.street"`). Jakarta Validation cascades through the nested beans via `@Valid`.

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

Produces `target/webforj-databinding.war`, ready to drop into any Servlet 6 container.

## Project layout

```
src/main/
├── java/com/webforj/databinding/
│   ├── Application.java             # webforJ entry (@Routify, @AppTheme, @StyleSheet)
│   ├── domain/
│   │   ├── Employee.java            # @Valid address + emergencyContact
│   │   ├── Address.java             # nested bean
│   │   └── EmergencyContact.java    # nested bean
│   └── views/
│       └── EmployeeFormView.java    # BindingContext.of + @UseProperty dotted paths
├── resources/
│   └── webforj.conf                 # webforJ entry / debug flags (HOCON)
└── webapp/
    └── WEB-INF/web.xml              # WebforjServlet mapped to /*
```

## How the binding works

```java
// Fields matching Employee property names bind automatically
private final TextField firstName = new TextField("First name");

// Nested properties are mapped with @UseProperty dotted paths
@UseProperty("address.street")
private final TextField street = new TextField("Street");

@UseProperty("emergencyContact.phone")
private final TextField phone = new TextField("Phone");

context = BindingContext.of(this, Employee.class, true);
context.read(employee);   // reads through nested gets
context.write(employee);  // creates missing nested beans via no-arg ctor, then writes
```

One `BindingContext.of()` call scans the view and binds everything. Jakarta validation messages declared on `Address`/`EmergencyContact` light up automatically because the `Employee` fields are annotated `@Valid`.

## Saving an employee

Clicking **Save** validates the form, writes the values into the `Employee` bean, and opens a `Dialog` showing the just-saved record. The dialog contains an `Accordion` with three panels — **Employee**, **Address**, and **Emergency contact** — and a primary-themed Close button.

## Learn more

- [Full Documentation](https://docs.webforj.com)
- [Data binding docs](https://docs.webforj.com/docs/data-binding/bindings) (nested-property pattern lands in 26.01)
- [Maven Jetty plugin](https://docs.webforj.com/docs/configuration/deploy-reload/maven-jetty-plugin)
