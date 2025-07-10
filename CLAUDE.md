# Claude Code Project Notes

## Project Overview
This is a webforJ dashboard application with multiple example projects (dashboard, explorer, howdy, tictactoe). The main focus has been on the dashboard application located in `webforj-dashboard/`.

## Design Principles & Preferences

### UI/UX Design
- **Flat Design**: No box-shadows on cards, use borders instead for visual separation
- **Responsive Design**: Mobile-first approach with proper breakpoints
- **Surface Levels**: Use surface-1, surface-2, surface-3 for visual hierarchy
- **Typography**: Black text on surface-2 backgrounds, gray subheaders on surface-3 for accessibility

### Component Architecture
- **BEM CSS Methodology**: Use Block__Element--Modifier naming convention
- **webforJ Framework**: Prefer webforJ components over custom HTML when available
- **webforJ MCP**: ALWAYS consult the webforJ MCP (https://mcp.webforj.com/mcp) before writing Java code to ensure proper API usage
- **Icon System**: Use FeatherIcon enum-based API (e.g., `FeatherIcon.GRID.create()`) instead of string-based API

### Code Style
- **No Code Comments**: Don't add comments unless explicitly requested
- **CSS Variables**: Use --dwc-* variables for consistent theming
- **Flex Layout**: Primary layout system for responsive design
- **Java Best Practices**: Follow webforJ component patterns and proper imports

## Key Components

### Portfolio Hero Section (Analytics)
- **Layout**: Horizontal flex with portfolio value (left), performance ring (center), compact stats (right)
- **Stats Cards**: Fixed 90px width, minimal padding, hidden on screens < 1068px
- **Responsive**: Stacks vertically on tablet, side-by-side on mobile
- **Performance Ring**: Circular progress indicator with YTD growth and mini metrics

### Navigation
- **Drawer Menu**: Flat structure (no nested items), Feather icons, auto-open
- **Theme Toggle**: Dark/light mode toggle in toolbar
- **Demo Banner**: Dismissible notification about demo data

## Technical Patterns

### CSS Organization
- Component-specific stylesheets (analytics-view.css, dashboard-view.css, etc.)
- Responsive breakpoints: 1068px, 768px, 600px, 480px
- Box-sizing: border-box for proper padding calculations

### Java Patterns
- Composite pattern for custom components extending FlexLayout
- Proper use of CSS classes instead of inline styles where possible
- ChartRedrawable interface for components with Google Charts

## Maintenance Notes
- All shadows removed from cards for flat design
- Stats cards in portfolio hero should remain compact (90px width)
- Always test responsive behavior at different breakpoints
- Demo data disclaimer should remain visible but unobtrusive

## Development Workflow
- **IMPORTANT**: Always consult webforJ MCP before writing Java code to ensure correct API usage
- Use TodoWrite/TodoRead tools for complex multi-step tasks
- Test responsive design at multiple screen sizes
- Prefer editing existing files over creating new ones
- Run lint/typecheck commands when available before committing changes

## CSS Best Practices
- **Minimal & Effective CSS**: Always aim for the most concise, direct solutions
- **Avoid Redundancy**: Don't duplicate CSS rules across media queries unnecessarily
- **Base Styles First**: Apply general fixes (like box-sizing, max-width) to base styles rather than repeating in media queries
- **Specific Only When Needed**: Use media queries only for properties that actually need to change at specific breakpoints
- **Consolidate Rules**: If multiple breakpoints need the same fix, apply it more broadly rather than repeating

## Responsive Design Patterns
- **Targeted Overrides**: Use CSS `:has()` selectors to target specific pages (e.g., `body:has(.dashboard-view)`) rather than global changes
- **Component-Level Padding**: Apply padding to specific wrappers when different components need different spacing behavior
- **Flex Order for Reordering**: Use `order` property with `!important` to reorder elements responsively (needed to override webforJ inline styles)
- **Progressive Enhancement**: Start with base styles that work everywhere, then add responsive enhancements
- **Container-Specific Solutions**: When global changes affect other pages, move overrides to component-specific CSS files