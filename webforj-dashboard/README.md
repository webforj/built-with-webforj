# Dashboard

A cryptocurrency market dashboard built with webforJ, with live-updating tables, charts and custom table cells.

![webforJ](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-dashboard%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'webforj.version'%5D&label=webforJ&color=blue)
![Java](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fwebforj%2Fbuilt-with-webforj%2Fmain%2Fwebforj-dashboard%2Fpom.xml&query=%2F%2F*%5Blocal-name()%3D'maven.compiler.source'%5D&label=Java&color=orange)

## Overview

The app has a market dashboard, analytics, news, settings and an about page. The dashboard shows summary cards with charts above a table of simulated cryptocurrencies whose prices refresh live. All cryptocurrency data, prices and news articles are randomly generated for demonstration and do not represent real market data.

## Features

- **Live price updates**: Prices in the table refresh continuously
- **Cryptocurrency table**: Simulated cryptocurrencies with custom cells for icons, price changes and inline sparkline charts
- **Charts**: Charts on the dashboard cards and in the analytics view, including portfolio allocation
- **News view**: Simulated news articles with search, category filtering and a trending section
- **Settings view**: Tabbed settings for general options, notifications, API keys and privacy
- **Responsive layout**: A collapsible navigation drawer adapts to the screen size
- **Theme toggle**: Switch between light and dark themes

## Getting started

### Prerequisites

- Java and Maven (see the badges for the Java version)

### Running the app

1. Clone the repository:
```bash
git clone https://github.com/webforj/built-with-webforj.git
cd built-with-webforj/webforj-dashboard
```

2. Run the application:
```bash
mvn jetty:run
```

3. Open `http://localhost:8080` in your browser.

## Learn more

- [Custom table cells with renderers in webforJ](https://docs.webforj.com/blog/table-renderers-custom-cells)
- [webforJ documentation](https://docs.webforj.com)
- [AppLayout](https://docs.webforj.com/docs/components/app-layout)
- [Google Charts](https://docs.webforj.com/docs/components/google-charts)
- [Table](https://docs.webforj.com/docs/components/table/overview)
