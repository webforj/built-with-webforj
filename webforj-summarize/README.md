# Summarize

A document summary demo built with webforJ and Spring AI, turning an uploaded file into a short bullet point summary.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-summarize%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-summarize%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-summarize%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

![Summarize screenshot](screenshot.png)

## Overview

This app demonstrates the webforJ file upload component together with Spring AI. Drop a PDF, Word, text or Markdown file, and the AI sums it up in a few bullet points. The whole flow is plain Java, with no REST controller and no JavaScript.

## Features

- **File upload**: The file is sent to the server as soon as it is picked or dropped
- **Text extraction**: The text is read out of PDF, Word, text and Markdown files
- **AI summary**: The model summarizes the document
- **Markdown output**: The summary renders as a bullet list
- **Background work**: The model call runs off the UI thread and the page updates when it finishes
- **Cleanup**: The uploaded file is deleted as soon as it has been summarized

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)
- A Gemini API key from [Google AI Studio](https://aistudio.google.com/apikey)

### API key setup

1. Go to [Google AI Studio](https://aistudio.google.com/apikey)
2. Sign in with your Google account
3. Create a new API key
4. Export it in the terminal you run the app from:

```bash
export SPRING_AI_GOOGLE_GENAI_API_KEY=YOUR_API_KEY_HERE
```

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-summarize
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

## Learn more

- [Summarize PDFs with AI](https://docs.webforj.com/blog/summarize-documents-spring-ai)
- [webforJ documentation](https://docs.webforj.com)
- [Upload component](https://docs.webforj.com/docs/components/upload)
- [MarkdownViewer component](https://docs.webforj.com/docs/components/markdownviewer)
- [Spring AI documentation](https://docs.spring.io/spring-ai/reference/)
