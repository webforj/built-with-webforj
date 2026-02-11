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
                }
            } catch (

            Exception e) {
                // Keep defaults
            }

            // Create brighter gradient for better dark mode visibility
            String gradient = String.format(
                    "linear-gradient(to bottom, rgba(%d,%d,%d,0.35), rgba(%d,%d,%d,0.5))",
                    r, g_val, b, r, g_val, b);

            // Calculate perceived luminance to determine text color contrast
            // Using WCAG formula: L = 0.299*R + 0.587*G + 0.114*B
            // Average the gradient's two opacity levels for calculation
            double avgOpacity = 0.425; // average of 0.35 and 0.5
            double perceivedLuminance = (0.299 * r + 0.587 * g_val + 0.114 * b) * avgOpacity;

            // Blend the genre color with white or black for harmonious text
            String textColor;
            if (perceivedLuminance > 70) {
                int textR = Math.max(0, (int) (r * 0.3));
                int textG = Math.max(0, (int) (g_val * 0.3));
                int textB = Math.max(0, (int) (b * 0.3));
                textColor = String.format("rgba(%d, %d, %d, 0.9)", textR, textG, textB);
            } else {
                int textR = Math.min(255, r + (int) ((255 - r) * 0.8));
                int textG = Math.min(255, g_val + (int) ((255 - g_val) * 0.8));
                int textB = Math.min(255, b + (int) ((255 - b) * 0.8));
                textColor = String.format("rgba(%d, %d, %d, 0.95)", textR, textG, textB);
            }

            // Brighter border with higher opacity
            String borderColor = String.format("rgba(%d,%d,%d,0.5)", r, g_val, b);

            html.append(String.format(
                    "<span class='genre-chip' style='background: %s; color: %s; border: 1px solid %s; border-radius: 9999px; padding: 2px 10px;'>%s</span>",
                    gradient, textColor, borderColor, genreName));
        }
        html.append("</div>");
        return html.toString();
    }
}
