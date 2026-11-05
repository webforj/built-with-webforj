# Howdy

A shared mood board built with webforJ, where each visitor picks a mood and sees everyone else's moods in a table and a chart.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-howdy%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-howdy%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

Howdy has three views behind a tabbed header: You, Dashboard and Users. On the You tab you pick a nickname and a mood and submit them. The Dashboard tab charts the mood counts, and the Users tab lists each nickname with its mood. All sessions share the same data, so changes from one visitor show up for the others.

## Features

- **Routing**: Each view has its own URL, and the dashboard is also the home page
- **Tab navigation**: A tabbed header moves between views and the header title follows the current view
- **Google Charts**: The dashboard shows the mood counts as a bar chart
- **Users table**: A table lists every nickname with its mood
- **Shared state**: Moods are shared between all sessions and the views refresh when they change
- **Nickname generator**: The app proposes a unique nickname
- **Empty state**: A friendly message shows when nobody has shared a mood yet

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-howdy
```

2. Run the application:
```bash
mvn jetty:run
```

3. Open `http://localhost:8080` in your browser.

## Learn more

- [webforJ documentation](https://docs.webforj.com)
- [Routing](https://docs.webforj.com/docs/routing/overview)
- [Google Charts](https://docs.webforj.com/docs/components/google-charts)
- [Namespaces](https://docs.webforj.com/docs/advanced/namespaces)
