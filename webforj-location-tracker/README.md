# Location tracker

A shipment tracking demo built with webforJ, showing browser geolocation and app badges.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-location-tracker%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-location-tracker%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

The browser location stands in for a dispatch hub. Each card shows the distance from the hub to its destination city, and the grid sorts nearest first. New shipments arrive unseen, and that unseen count drives every badge in the app.

## Features

- **Geolocation**: Reads the browser location and falls back gracefully when permission is declined or unanswered
- **Favicon badge**: The unseen count is drawn over the browser tab icon
- **App badge**: The unseen count shows on the OS-level app icon when the app is installed
- **Badge component**: The header bell carries a badge with the unseen count
- **Busy indicator**: A busy overlay covers the location request so the grid appears already sorted
- **Card layout**: Each shipment is a card with a photo, title, caption, body and footer

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-location-tracker
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

Allow the location prompt to see distances from your position. If you decline or the prompt goes unanswered, the shipments show without distances.

## Learn more

- [webforJ documentation](https://docs.webforj.com)
- [Geolocation](https://docs.webforj.com/docs/advanced/geolocation)
- [App badges](https://docs.webforj.com/docs/advanced/app-badges)
- [Card component](https://docs.webforj.com/docs/components/card)
- [Badge component](https://docs.webforj.com/docs/components/badge)
