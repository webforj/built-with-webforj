# Focus Tracker

A Pomodoro-style focus timer built with webforJ, showcasing Progressive Web App capabilities including installable apps, desktop notifications, and the App Badge API.

![webforJ Version](https://img.shields.io/badge/webforJ-25.10-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-green)
![Java](https://img.shields.io/badge/Java-21-orange)

## Overview

This app demonstrates how webforJ enables Java developers to access modern web platform capabilities. A simple focus timer serves as the vehicle to showcase PWA features that were traditionally only available to JavaScript developers.

## What You'll Learn

- **Installable Web Apps**: Use `@AppProfile` to make your app installable on desktop and mobile
- **Desktop Notifications**: Send native system notifications with `DesktopNotification.show()`
- **App Badge API**: Display dynamic badges on the app icon via `Page.executeJsVoidAsync()`
- **Interval Timer**: Use webforJ's `Interval` class for periodic tasks

## Features

- **Adjustable Timer**: +/- buttons to set focus duration (1-60 minutes)
- **Badge Updates**: App icon badge shows remaining minutes
- **Desktop Notification**: Alert when session completes
- **Clean UI**: Responsive design using FlexLayout and CSS variables

## Tech Stack

- **Frontend**: webforJ 25.10
- **Backend**: Spring Boot 3.5.8
- **Build Tool**: Maven
- **Java Version**: 21

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+

### Running the App

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-focustracker
```

2. Run the application:
```bash
mvn spring-boot:run
```

3. Open your browser and navigate to:
```
http://localhost:8080
```

### Installing as PWA

To see the Badge API in action:

1. Open the app in Chrome or Edge
2. Click the install icon in the address bar
3. Launch the installed app
4. Start a focus session - the badge will show on the app icon

## Key Code Examples

### Making the App Installable

```java
@AppProfile(name = "Focus Tracker", shortName = "Focus")
public class Application extends App {
}
```

### Sending Desktop Notifications

```java
DesktopNotification.show("Focus Session Complete!", "Great work! Take a break.");
```

### Setting the App Badge

```java
Page.getCurrent().executeJsVoidAsync("navigator.setAppBadge(" + minutes + ")");
```

### Using Interval for Timers

```java
timerInterval = new Interval(1f, e -> tick());
timerInterval.start();
```

## Learn More

- [webforJ Documentation](https://docs.webforj.com)
- [App Badge API](https://developer.chrome.com/docs/capabilities/web-apis/badging-api)
- [Desktop Notifications](https://docs.webforj.com/docs/components/desktop-notification)
