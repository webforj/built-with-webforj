# InvoiceReader

An invoice extraction demo built with webforJ and Spring AI, turning an invoice image into typed Java fields.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-invoicereader%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Spring Boot](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-invoicereader%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D&label=Spring%20Boot&color=green)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-invoicereader%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

![InvoiceReader screenshot](screenshot.png)

## Overview

This app demonstrates the webforJ file upload component together with a vision-capable model through Spring AI. Upload a photo or scan of an invoice, and the AI returns the vendor, invoice number, date, currency and total as typed fields. There is no separate OCR library and no text parsing step.

## Features

- **Image upload**: Pick or drop an invoice image and it is sent right away
- **Sample invoice**: A bundled sample invoice lets you try the app without your own file
- **Image preview**: The uploaded invoice shows on the page while the AI reads it
- **Structured output**: The model reads the image and the answer maps straight to Java fields
- **Background work**: The model call runs off the UI thread and the page updates when it finishes
- **Cleanup**: The image is deleted as soon as the model has read it

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
cd built-with-webforj/webforj-invoicereader
```

2. Run the application:
```bash
mvn
```

3. Open `http://localhost:8080` in your browser.

## Learn more

- [Extract data from an image](https://docs.webforj.com/blog/ai-image-data-extraction-java)
- [webforJ documentation](https://docs.webforj.com)
- [Upload component](https://docs.webforj.com/docs/components/upload)
- [Spring AI structured output](https://docs.spring.io/spring-ai/reference/api/structured-output-converter.html)
