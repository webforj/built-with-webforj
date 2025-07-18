# webforJ Dashboard

A cryptocurrency market monitoring app built with webforJ, demonstrating advanced data visualization, real-time rendering capabilities, and modern UI patterns using webforJ's library of components.

## Overview

The webforJ Dashboard showcases how to build a professional data-driven web app with webforJ. It features a cryptocurrency market dashboard with real-time data updates, financial news aggregation, portfolio analytics, and comprehensive user settings.

**Important:** All cryptocurrency data, prices, news articles, and market information displayed in this app are randomly generated for demonstration purposes and do not represent actual market data.

## Main Features

- **Real-time Data Rendering** - Tables and charts automatically update with new data points
- **Interactive Data Visualization** - Google Charts integration for portfolio allocation, trading volumes, and market trends
- **Dynamic Cryptocurrency Table** - Monitor 40+ simulated cryptocurrencies with automatic price updates and market metrics
- **Financial News Hub** - Simulated cryptocurrency news with search, category filtering, and trending articles
- **Responsive Design** - Mobile-optimized layouts with collapsible navigation drawer with webforJ's [`AppLayout`](https://docs.webforj.com/docs/components/app-layout)
- **Theme Support** - Light/dark mode toggle
- **Advanced Table Rendering** - Custom renderers for icons, price changes, and inline sparkline charts

## Technical Highlights

This app demonstrates several advanced webforJ features:
- Real-time UI updates using intervals
- Complex component composition and reusable UI components
- Google Charts integration with custom styling and responsive behavior
- Advanced table features with custom cell renderers
- Service layer architecture with simulated market data generation

## Prerequisites

- Java 21 or newer
- Maven 3.9+

## Getting Started

1. Clone the repository and navigate to the webforj-dashboard directory:
   ```bash
   cd webforj-dashboard
   ```

2. Run the app:
   ```bash
   mvn jetty:run
   ```

3. Open [http://localhost:8080](http://localhost:8080) in your browser

## Project Structure

```
src/main/java/com/webforj/builtwithwebforj/dashboard/
├── components/          # Reusable UI components
│   ├── analytics/      # Chart cards and portfolio components
│   ├── dashboard/      # Cryptocurrency table and cards
│   ├── news/          # News cards and sections
│   └── settings/      # Settings panel components
├── models/            # Data models (Cryptocurrency, NewsArticle)
├── services/          # Business logic and data services
├── utils/             # Formatters, renderers, and chart utilities
└── views/             # App views and layouts
```

## Learn More

- [webforJ Documentation](https://docs.webforj.com)
- [Component Overview](https://docs.webforj.com/docs/components/overview)
- [Google Charts in webforJ](https://docs.webforj.com/docs/components/googlecharts)
- [Table Component Guide](https://docs.webforj.com/docs/components/table)