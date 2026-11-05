# Terminal

A browser terminal built with webforJ, with typed commands, command history and a playable Snake game.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-terminal%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-terminal%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-terminal%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

<video src="https://cdn.webforj.com/webforj-documentation/video/social/snake-terminal.mp4" controls muted playsinline width="100%">
  <a href="https://cdn.webforj.com/webforj-documentation/video/social/snake-terminal.mp4">Watch the Terminal demo video</a>
</video>

## Overview

This app demonstrates the webforJ Terminal component. Every keystroke reaches Java as an event, Java parses the command line and writes the output back with ANSI colors and cursor moves. Commands are plain Java classes behind one interface, so adding a new one takes a single class.

## Features

- **Commands**: `help`, `clear`, `time`, `history`, `msg <text>` and `prompt <question>`
- **Snake game**: Type `snake`, steer with the arrow keys and press `q` to quit
- **History**: The up and down arrows walk through earlier commands
- **Dialogs**: `msg` and `prompt` open webforJ option dialogs from the terminal
- **ANSI output**: Colors, box drawing and cursor positioning for the game board

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-terminal
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser, select **Open Terminal** and type `help`.

## Learn more

- [webforJ documentation](https://docs.webforj.com)
- [Terminal component](https://docs.webforj.com/docs/components/terminal)
- [Option dialogs](https://docs.webforj.com/docs/components/option-dialogs/overview)
