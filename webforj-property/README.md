# PropertyView Demo

A real estate property listing demo application showcasing the webforJ Map component. This demo demonstrates interactive map features, property filtering, and responsive UI patterns.

## Features

- **Interactive Map** - OpenLayers-based map with custom property markers
- **Property Filtering** - Filter by property type, bedrooms, bathrooms, and search by address
- **Property Cards** - Scrollable list with property details, status badges, and hover effects
- **Detail View** - Click any property to view full details including description and stats
- **Map-List Sync** - Selecting a property in the list highlights it on the map and vice versa
- **Empty State** - Informative feedback when no properties match the current filters

## Tech Stack

- **[webforJ](https://webforj.com/)** - Java framework for building web applications
- **webforJ Map Component** - OpenLayers-based interactive map component
- **Java 21** - Modern Java features
- **Maven** - Build and dependency management
- **Jetty** - Embedded servlet container for development

## Prerequisites

- Java 21 or newer
- Maven 3.9+

## Getting Started

To run the application in development mode:

```bash
mvn jetty:run
```

Then open [http://localhost:8080](http://localhost:8080) in your browser.

## Project Structure

```
src/main/java/com/webforj/property/
├── Application.java          # Main application entry point
├── components/               # Reusable UI components
│   ├── FilterBar.java        # Property filter controls
│   ├── FilterChip.java       # Filter tag chips
│   ├── MapPanel.java         # Map wrapper component
│   ├── PropertyCard.java     # Property list card
│   ├── PropertyDetailView.java # Property detail panel
│   └── Sidebar.java          # Main sidebar container
├── data/
│   └── PropertyDataProvider.java # Sample property data
├── model/                    # Data models
│   ├── FilterState.java
│   ├── Property.java
│   ├── PropertyStatus.java
│   └── PropertyType.java
└── views/
    ├── MainLayout.java       # Main application layout
    └── PropertyView.java     # Property listing view
```

## Building for Production

To create a WAR file for deployment:

```bash
mvn clean package -Pprod
```

The WAR file will be created in `target/webforj-property-<version>.war`

## Learn More

- [webforJ Documentation](https://docs.webforj.com/)
- [webforJ GitHub](https://github.com/webforj/webforj)
- [webforJ Map Component](https://docs.webforj.com/components/map)
