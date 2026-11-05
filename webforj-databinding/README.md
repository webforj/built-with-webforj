# Data binding

A data binding demo built with webforJ that binds one form to an employee record with nested address and emergency contact details.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-databinding%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-databinding%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

The app is a single employee onboarding form with employee, address and emergency contact sections. One binding wires every field, and validation checks the whole record. Clicking Save validates the form and opens a dialog showing the saved record.

## Features

- **Automatic binding**: Every field in the view is bound in one step
- **Nested properties**: Fields map to properties of nested records
- **Cascading validation**: Validation messages on the nested records apply to the form
- **Live save button**: The Save button is enabled only while the form is valid
- **Saved record dialog**: A dialog with an accordion shows the saved employee, address and emergency contact
- **Responsive layout**: Fields lay out in one or two columns depending on the width

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-databinding
```

2. Run the application:
```bash
mvn jetty:run
```

3. Open `http://localhost:8080` in your browser.

## Learn more

- [Nested data binding in webforJ](https://docs.webforj.com/blog/nested-data-binding-webforj)
- [webforJ documentation](https://docs.webforj.com)
- [Data binding](https://docs.webforj.com/docs/data-binding/bindings)
- [Maven Jetty plugin](https://docs.webforj.com/docs/configuration/deploy-reload/maven-jetty-plugin)
