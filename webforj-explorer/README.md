# Explorer

A code viewer built with webforJ that mimics the VS Code layout, with a file tree on the left and a read-only code editor on the right.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-explorer%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-explorer%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

When the app opens, it asks you to pick a directory. The directory becomes a file tree in the side drawer, and selecting a file shows its contents with syntax highlighting. It shows a lazy-loading tree and how to wrap a third-party web component for use in webforJ.

## Features

- **File tree**: Built from the chosen directory, with directories listed before files
- **Lazy loading**: A directory loads its children the first time it is expanded
- **File icons**: Common file types get matching icons
- **Syntax highlighting**: The editor language is chosen from the file extension
- **Read-only code editor**: Monaco Editor wrapped as a webforJ component
- **File tooltips**: Each node shows file details in a tooltip
- **Directory picker**: A directory chooser dialog, shown again if you cancel

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-explorer
```

2. Run the application:
```bash
mvn jetty:run
```

3. Open `http://localhost:8080` in your browser.

4. When the dialog appears, choose a directory to explore. The app reads files from the machine that runs the server.

## Learn more

- [webforJ documentation](https://docs.webforj.com)
- [Tree](https://docs.webforj.com/docs/components/tree)
- [AppLayout](https://docs.webforj.com/docs/components/app-layout)
- [FileChooserDialog](https://docs.webforj.com/docs/components/option-dialogs/file-chooser)
