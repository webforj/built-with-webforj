# AskDocs

A retrieval augmented generation (RAG) demo built with webforJ and Spring AI that answers questions only from the documents you add and lists the files it used.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-askdocs%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-askdocs%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-askdocs%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

![AskDocs screenshot](screenshot.png)

## Overview

Add files, ask a question, and the answer comes only from those files, with the sources shown underneath. The whole app is written in Java and shows file upload, streaming updates to the UI and Spring AI integration.

## Features

- **Document upload**: Files are sent for processing as soon as you drop them
- **Retrieval augmented generation**: The most relevant parts of your documents are found and given to the model to answer from
- **Streaming answers**: The answer appears progressively as it is written
- **Sources**: The files an answer was based on appear as chips under it
- **Clear states**: Empty, working, answered and failed states are shown
- **Sample handbook**: A bundled travel handbook lets you try the app without your own file

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)
- A Gemini API key from [Google AI Studio](https://aistudio.google.com/apikey)

### API key setup

The app needs the key twice, once for the chat model and once for the embedding model. Set both in your environment, never in a file you commit:

```bash
export SPRING_AI_GOOGLE_GENAI_API_KEY=YOUR_API_KEY_HERE
export SPRING_AI_GOOGLE_GENAI_EMBEDDING_API_KEY=YOUR_API_KEY_HERE
```

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-askdocs
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

Click *Try the sample handbook*, then click one of the example questions.

## Learn more

- [Spring AI RAG web UI](https://docs.webforj.com/blog/spring-ai-rag)
- [webforJ documentation](https://docs.webforj.com)
- [Upload component](https://docs.webforj.com/docs/components/upload)
- [MarkdownViewer component](https://docs.webforj.com/docs/components/markdownviewer)
- [Asynchronous updates](https://docs.webforj.com/docs/advanced/asynchronous-updates)
- [Spring AI retrieval augmented generation](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html)
