# Focus tracker

A Pomodoro-style focus timer built with webforJ and Spring Boot.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-focustracker%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-focustracker%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-focustracker%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

This app shows how webforJ gives Java developers access to modern web platform capabilities. Set a duration, start the timer, and the remaining minutes appear on the browser favicon and on the installed app icon. When the session ends, a desktop notification tells you to take a break.

## Features

- **Installable app**: Install the app on desktop and mobile
- **Adjustable timer**: Plus and minus buttons set the focus duration
- **Badge updates**: The installed app icon and the browser favicon show the remaining minutes
- **Desktop notification**: An alert tells you when the session completes
- **Live countdown**: The timer counts down every second
- **Responsive layout**: The UI stays clean at any width

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-focustracker
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

### Trying the badges

The browser favicon badge works without installing the app. To also see the badge on the operating system app icon:

1. Open the app in Chrome or Edge
2. Click the install icon in the address bar
3. Launch the installed app
4. Adjust or start the timer. The remaining minutes show on both the browser favicon and the app icon

## Learn more

- [Installable web apps with webforJ](https://docs.webforj.com/blog/installable-apps)
- [webforJ documentation](https://docs.webforj.com)
- [webforJ app badges](https://docs.webforj.com/docs/advanced/app-badges)
- [Desktop notifications](https://docs.webforj.com/docs/components/desktop-notification)
- [Badging API](https://developer.chrome.com/docs/capabilities/web-apis/badging-api)
