package com.webforj.bookstore.components;

import com.webforj.component.table.renderer.Renderer;

/**
 * A table cell renderer that displays genre names as colored chip badges.
 *
 * @param <T> the type of the table row entity
 */
public class GenreChipRenderer<T> extends Renderer<T> {

  @Override
  public String build() {
    return """
                <div style='display:flex;gap:var(--dwc-space-xs);flex-wrap:wrap;'>
                  <% var genres = JSON.parse(cell.value || '[]'); %>
                  <% genres.forEach(function(g) { %>
                    <span style='display:inline-flex;align-items:center;justify-content:center;gap:6px;\
        padding:2px 10px;border-radius:9999px;background:transparent;\
        border:1px solid var(--dwc-border-color-default);font-size:var(--dwc-font-size-xs);\
        font-weight:600;line-height:1.5;'>
                      <span style='width:8px;height:8px;border-radius:50%;background:<%- g.color %>;flex-shrink:0;'></span>
                      <span><%- g.name %></span>
                    </span>
                  <% }); %>
                </div>
                """;
  }
}
