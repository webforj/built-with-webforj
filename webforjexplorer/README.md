# webforJ Explorer

A showcase application demonstrating the file navigation and code viewing capabilities of the webforJ framework.

## Overview

webforJ Explorer is a minimal code viewer that mimics the VSCode layout. It uses webforJ’s layout system, a file tree on the left, and a Monaco editor to display file contents in read-only mode. This project shows how to integrate dynamic data, build a tree UI, and embed third-party components.

<div align='center'>
  <video src="https://github.com/user-attachments/assets/33c17a21-6880-4f0d-a1a2-d39d023d1615" width="400" > </video>
</div>

### **Main Features**

- **Sidebar Layout (VSCode-style)**
  - Left-hand menu shows a **file tree** based on a directory selected by the user.
  - Folders are expandable/collapsible
  - Icons for common file types (e.g., `.js`, `.java`, `.html`, `.md`)
- **File Tree**
  - Built dynamically from a user-selected directory
  - Clicking a file displays its contents
- **Main Editor Area**

  - Shows file content using **Monaco Editor**
  - Editor is in **read-only** mode
  - Syntax highlighting based on file extension

### **Data Handling**
- Directory tree can be mocked with static data, or populated using a file API (if applicable)
- File contents can be preloaded for the mock version

## Technical Highlights

This application demonstrates several webforJ capabilities:

- **Component Integration**: Shows how to wrap external web components (Monaco Editor) as webforJ components
- **Event Handling**: Implements tree selection and expansion events for interactive navigation
- **Lazy Loading**: Efficient directory content loading only when expanded
- **Custom Styling**: Integration with CSS and icon libraries
- **Routing**: Uses webforJ's annotation-based routing system
- **File Operations**: Safe file reading and metadata extraction

## Prerequisites

- Java 21 or newer
- Maven 3.9+

## Getting Started

1. Clone the repository:

   ```bash
   git clone [repository-url]
   cd webforjexplorer
   ```

2. Run the application in development mode:

   ```bash
   mvn jetty:run
   ```

3. Open [http://localhost:8080](http://localhost:8080) in your browser

4. When prompted, select a directory to explore

## Project Structure

```
webforjexplorer/
├── src/main/java/com/webforjexplorer/
│   ├── Application.java          # Main application entry point
│   ├── components/
│   │   └── Editor.java          # Monaco Editor wrapper component
│   ├── utils/
│   │   └── Utility.java         # File operations and helper methods
│   └── views/
│       └── HomeView.java        # Main file explorer view
├── src/main/resources/
│   ├── config/
│   │   └── mapping.json         # File extension to icon/language mappings
│   └── static/
│       └── app.css              # Application styles
└── pom.xml                      # Maven configuration
```

## Configuration

### File Type Mappings

The application uses `mapping.json` to configure:

- **Icon mappings**: Maps file extensions to Tabler icons
- **Language mappings**: Maps file extensions to Monaco Editor language identifiers

Example configuration:

```json
{
  "iconMappings": {
    "java": "coffee",
    "json": "braces",
    "html": "world"
  },
  "fileLanguageMappings": {
    "java": "java",
    "json": "json",
    "html": "html"
  }
}
```

## Architecture

The application follows a clean architecture pattern:

- **Views**: Handle UI layout and user interactions
- **Components**: Reusable UI components with specific functionality
- **Utils**: Utility methods for common operations
- **Resources**: Configuration files and static assets

## Learn More

Explore the webforJ ecosystem:

- [Full Documentation](https://docs.webforj.com)
- [Component Overview](https://docs.webforj.com/docs/components/overview)
- [Quick Tutorial](https://docs.webforj.com/docs/introduction/tutorial/overview)
- [Advanced Topics](https://docs.webforj.com/docs/advanced/overview)
