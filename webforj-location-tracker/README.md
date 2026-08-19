# Location Tracker

A shipment tracking demo built with webforJ, showcasing the browser Geolocation API and the three App Badge surfaces.

![webforJ Version](https://img.shields.io/badge/webforJ-26.02-blue)
![Java](https://img.shields.io/badge/Java-21-orange)

## Overview

The browser location stands in for a dispatch hub. Each card shows the great-circle distance from the hub to its destination city, and the grid sorts nearest-first. New shipments arrive unseen, and that unseen count drives every badge in the app.

## Features

- **Geolocation**: `Geolocation.getCurrentPosition()` with a timeout and a deadline for an unanswered permission prompt
- **Favicon badge**: `Page.getCurrent().setIconBadge(count)` draws the count over the browser tab icon
- **App badge**: `App.setBadge(count)` writes the OS-level badge when installed as a PWA
- **Badge component**: `Button.setBadge()` slots a `dwc-badge` into the header bell
- **Busy indicator**: `App.busy()` covers the location request so the grid renders already sorted
- **Card layout**: `Card` slots for the hero photo, title, caption, body and footer

## Tech Stack

- **Frontend**: webforJ 26.02
- **Build Tool**: Maven
- **Java Version**: 21

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+

### Run

```bash
mvn
```

Open [http://localhost:8080](http://localhost:8080).
