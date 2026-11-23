# AGENTS.md

This is a webforJ project that brings its own design system: the UI is built in Java from wrappers around the SAP UI5 Web Components, not from the built-in webforJ components.

## Commands

- `mvn` — dev mode. The default goal (`compile webforj:watch <run>`) installs the UI5 npm packages, compiles the frontend, watches `src/main/frontend`, and serves the app.
- `mvn test` — run the tests.
- `mvn package` — build the executable JAR into `target/`.
- Run the JAR with `--spring.profiles.active=prod` for production overrides. Without a profile it uses shared defaults.

Java version, dependencies, plugins, and the run goal are declared in `pom.xml` — read them there instead of assuming.

## Layout

- `src/main/java/.../Application.java` — entry point, `extends App`. `@Routify` scans the `views` package and `@BundleEntry("app.css")` loads the app stylesheet.
- `src/main/java/.../components/` — the UI5 wrappers. Each extends `ElementComposite` and declares its npm package with `@BundlePackage` and its module with `@BundleEntry`.
- `src/main/java/.../views/` — routes. Each is a `@Route`-annotated `Composite`.
- `src/main/frontend/` — frontend sources compiled by the webforJ Maven plugin. `app.css` holds the page layout, and `ui5/` holds the entries of the components that need another module loaded first: the icon collection before a component that shows icons, and the locale data before the date picker.
- `src/main/resources/` — `webforj.conf`, or `application.properties` on Spring, for runtime config.

## webforJ MCP server

Already configured in this repo at `https://mcp.webforj.com/mcp`.

- Resolve the webforJ version from `pom.xml`, and scope every answer to it.
- Look up webforJ classes, methods, and annotations through the server — don't guess them.

## Testing

Tests use the webforJ TestBed and live in `src/test/java/.../views/` as `<View>Test.java`, with the wrapper tests in `src/test/java/.../components/`. `mvn test` runs them, without a browser or a server. A test class carries `@WebforjTest(app = Application.class)`, receives a `WebforjTestbed` parameter, navigates with `context.navigate(View.class)`, interacts through `UserInteraction` and finds components through `ComponentLocator`. A UI5 wrapper is driven with `UserInteraction.fireEvent(component, "<browser event>", Map.of("<property>", value))`. Add one test class per view.

## Do's and Don'ts

- **Do** build views only from the `Ui5*` wrappers, and add a wrapper when a view needs another UI5 component.
- **Do** implement the webforJ concern interfaces (`HasText`, `HasEnablement`, `HasPlaceholder`, `HasVisibility` and so on) a UI5 element supports, instead of inventing setter names. craftforJ reads them.
- **Do** take colors and fonts from the UI5 theme through its `--sap*` CSS variables, and put layout rules in `src/main/frontend/app.css` as classes.
- **Do** check a UI5 component's properties, slots and events in the UI5 Web Components documentation before wrapping it.
- **Do** add a `<View>Test` for each new view, and run `mvn test` before finishing.
- **Don't** style components from Java with `setStyle`, and don't mix built-in webforJ UI components into the views.
- **Don't** guess webforJ APIs — resolve them through the MCP server.
- **Don't** edit anything under `target/` or other generated output.
- **Don't** add dependencies or change the build without checking `pom.xml` first.
