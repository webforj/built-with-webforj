# Tic-tac-toe

A two-player Tic-tac-toe game built with webforJ, where players on separate browsers share one Java game object through webforJ namespaces.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-tictactoe%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-tictactoe%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

One player starts a game and receives a game key. The second player enters that key to join, and both take turns on the same board. The game uses no database, REST API or WebSocket code. The game state is shared between players through a webforJ namespace, and each board updates live.

## Features

- **Start and join**: A host gets a generated game key to share, and a guest joins by entering it
- **Shared game state**: One game is shared between players in different browsers
- **Live updates**: Each board redraws as soon as the game changes
- **Route parameters**: The game page URL carries the game key, nickname and action
- **Navigation guard**: An invalid game key blocks navigation and shows a toast
- **Sound effects**: Click, win, lose, draw and tap sounds play during the game

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-tictactoe
```

2. Run the application:
```bash
mvn jetty:run
```

3. Open `http://localhost:8080` in your browser.

To play, open the app in two browser windows. Start a game in the first, copy the game key, and join with it in the second.

## Learn more

- [webforJ documentation](https://docs.webforj.com)
- [Namespaces](https://docs.webforj.com/docs/advanced/namespaces)
- [Routing overview](https://docs.webforj.com/docs/routing/overview)
- [Route patterns](https://docs.webforj.com/docs/routing/route-patterns)
