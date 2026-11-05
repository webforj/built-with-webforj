# PingPal

An API testing tool built with webforJ for composing, sending and saving HTTP requests.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fpingpal%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fpingpal%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

PingPal is a Postman-inspired tool for testing and managing API requests. You build a request, send it and read the formatted response. Saved requests are loaded from and stored in a separate backend API that the app talks to over HTTP. It shows routing and a rich single-page interface built in Java.

## Features

- **HTTP requests**: Create and manage GET, POST, PUT and DELETE requests
- **Authentication**: Configure Basic, Bearer or API key authentication
- **Headers and bodies**: Set custom headers and request bodies, with a JSON editor for the body
- **Formatted responses**: View the response body, headers, status code, size and duration
- **Request history**: Reopen saved requests from the sidebar
- **Console**: Follow request activity in a built-in console view

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)
- A running PingPal API server

### Environment setup

The app reads its API settings from a `.env` file in the working directory, or from system environment variables:

```
PINGPAL_URL=<base URL of the API server>
PINGPAL_USERNAME=<API username>
PINGPAL_PASSWORD=<API password>
```

The app signs in to the API server with these credentials and uses it to load and save requests.

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/pingpal
```

2. Run the application:
```bash
mvn jetty:run
```

3. Open `http://localhost:9965` in your browser.

## Credits

PingPal was created by [Rick van Baalen](https://www.linkedin.com/in/rickvanbaalen/) and won first place in the Built with webforJ contest. The application code is included as submitted, to preserve the contest-winning entry.

## Learn more

- [webforJ documentation](https://docs.webforj.com)
- [Routing overview](https://docs.webforj.com/docs/routing/overview)
- [Route patterns](https://docs.webforj.com/docs/routing/route-patterns)
