package org.example.utils;

import org.example.models.Cryptocurrency;

import com.webforj.component.table.renderer.Renderer;

public class PriceChartRenderer extends Renderer<Cryptocurrency> {
    @Override
    public String build() {
      return /* html */"""
            <%
              const history = JSON.parse(cell.row.getValue('PriceHistory'));
              const values = history.slice(-20); // Last 20 data points
              const min = Math.min(...values);
              const max = Math.max(...values);
              const range = max - min || 1;
              const svgWidth = 200; // Viewbox width for calculations
              const svgHeight = 35;

              // Generate sparkline path
              const points = values.map((v, i) => {
                const x = (i / (values.length - 1)) * svgWidth;
                const y = svgHeight - ((v - min) / range) * svgHeight;
                return `${x},${y}`;
              }).join(' ');

              // Determine color based on price change
              const priceChange = cell.row.getValue('PriceChangePercentage24h');
              const color = priceChange >= 0 ? 'rgb(34, 197, 94)' : 'rgb(239, 68, 68)';
            %>
            <div part="sparkline-cell" style="width: 100%; display: flex;">
              <svg width="100%" height="<%= svgHeight %>" viewBox="0 0 <%= svgWidth %> <%= svgHeight %>" preserveAspectRatio="none" style="width: 100%;">
                <polyline
                  points="<%= points %>"
                  fill="none"
                  stroke="<%= color %>"
                  stroke-width="2"
                  stroke-linejoin="round"
                  stroke-linecap="round"
                  vector-effect="non-scaling-stroke"
                />
              </svg>
            </div>
          """;
    }
  }