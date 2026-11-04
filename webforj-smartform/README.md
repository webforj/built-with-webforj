# SmartForm

A form-filling demo built with webforJ and Spring AI, turning an email or document into a validated support ticket.

![webforJ Version](https://img.shields.io/badge/webforJ-26.03-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-green)
![Java](https://img.shields.io/badge/Java-21-orange)

![SmartForm screenshot](screenshot.png)

## Overview

This app demonstrates webforJ data binding together with Spring AI structured output. Paste an email or drop a file, and the AI drafts a support ticket. The user reviews the fields, and Jakarta Validation decides what can be saved.

## Features

- **AI Form Filling**: Gemini extracts the ticket fields into a Java bean through Spring AI structured output
- **File Upload**: Reads PDF, Word, email and text files with the `Upload` component and Apache Tika
- **Data Binding**: `BindingContext.of` binds the form fields to the bean by name
- **Validation**: The same Jakarta Validation rules check typed and AI-filled values
- **Background Work**: The model call runs in a Spring `@Async` method and updates the UI with `Environment.runLater`
- **Human in the Loop**: Nothing is saved until the user reviews the ticket and clicks Save

## Tech Stack

- **Frontend**: webforJ 26.03
- **Backend**: Spring Boot 4.1.0 with Spring AI 2.0
- **LLM**: Google Gemini via Spring AI
- **Build Tool**: Maven
- **Java Version**: 21

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Gemini API key

### API Key Setup

This app uses Google Gemini as the LLM. To get your API key:

1. Go to [Google AI Studio](https://aistudio.google.com/apikey)
2. Sign in with your Google account
3. Create a new API key
4. Export it in the terminal you run the app from:

```bash
export SPRING_AI_GOOGLE_GENAI_API_KEY=YOUR_API_KEY_HERE
```

### Running the App

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-smartform
```

2. Run the application:
```bash
mvn
```

3. Open your browser and navigate to:
```
http://localhost:8080
```

## Learn More

- [webforJ Documentation](https://docs.webforj.com)
- [webforJ Data Binding](https://docs.webforj.com/docs/data-binding/overview)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
