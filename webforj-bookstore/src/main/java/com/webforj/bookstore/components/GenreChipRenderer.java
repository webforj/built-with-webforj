package com.webforj.bookstore.components;

import com.webforj.bookstore.domain.Genre;
import com.webforj.bookstore.service.GenreService;
import java.util.List;

/**
 * Renders genre chips as HTML string for the InventoryView table.
 */
public class GenreChipRenderer {

    private final GenreService genreService;

    public GenreChipRenderer(GenreService genreService) {
        this.genreService = genreService;
    }

    public String render(List<String> genreNames) {
        if (genreNames == null || genreNames.isEmpty()) {
            return "";
        }
        StringBuilder html = new StringBuilder("<div class='genre-chips-container'>");
        for (String genreName : genreNames) {
            Genre g = genreService.getAllGenresSorted().stream()
                    .filter(gen -> gen.getName().equals(genreName))
                    .findFirst().orElse(null);

            String baseColor = (g != null && g.getColor() != null && !g.getColor().isEmpty())
                    ? g.getColor()
                    : "rgba(107, 107, 107, 1)"; // gray default

            // Parse rgba color to extract r, g, b values
            int r = 107, g_val = 107, b = 107; // default gray
            try {
                if (baseColor.startsWith("rgba(") || baseColor.startsWith("rgb(")) {
                    String values = baseColor.substring(baseColor.indexOf('(') + 1, baseColor.indexOf(')'));
                    String[] parts = values.split(",");
                    if (parts.length >= 3) {
                        r = Integer.parseInt(parts[0].trim());
                        g_val = Integer.parseInt(parts[1].trim());
                        b = Integer.parseInt(parts[2].trim());
                    }
                } else if (baseColor.startsWith("#")) {
                    // Handle hex color format (#RRGGBB)
                    String hex = baseColor.substring(1);
                    if (hex.length() == 6) {
                        r = Integer.parseInt(hex.substring(0, 2), 16);
                        g_val = Integer.parseInt(hex.substring(2, 4), 16);
                        b = Integer.parseInt(hex.substring(4, 6), 16);
                    }
                }
            } catch (Exception e) {
                // Keep defaults
            }

            // Create vibrant circle color
            String circleColor = String.format("rgb(%d, %d, %d)", r, g_val, b);

            // Chip with small colored circle prefix - default background, centered text
            html.append(String.format(
                    "<span class='genre-chip' style='display: inline-flex; align-items: center; justify-content: center; gap: 6px; padding: 2px 10px; border-radius: 9999px; background: transparent; border: 1px solid var(--dwc-border-color-default); color: var(--dwc-color-text);'>"
                            + "<span style='width: 8px; height: 8px; border-radius: 50%%; background: %s; flex-shrink: 0;'></span>"
                            + "<span>%s</span>"
                            + "</span>",
                    circleColor, genreName));
        }
        html.append("</div>");
        return html.toString();
    }
}
