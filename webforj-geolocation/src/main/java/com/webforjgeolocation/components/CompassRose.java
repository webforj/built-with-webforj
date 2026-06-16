package com.webforjgeolocation.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import java.util.Locale;

public class CompassRose extends Composite<Div> {

  private final Div self = getBoundComponent();
  private double accumulatedRotation = 0.0;

  public CompassRose() {
    self.addClassName("compass");
    self.setAttribute("role", "img");
    self.setAttribute("aria-label",
        "Compass rose; the needle points toward the selected wonder");
    self.setHtml(buildSvg());
  }

  public CompassRose setBearing(double degrees) {
    double current = accumulatedRotation % 360.0;
    double delta = ((degrees - current) + 540.0) % 360.0 - 180.0;
    accumulatedRotation += delta;
    self.setStyle("--bearing",
        String.format(Locale.US, "%.2fdeg", accumulatedRotation));
    return this;
  }

  private static String buildSvg() {
    StringBuilder ticks = new StringBuilder();
    for (int deg = 0; deg < 360; deg += 6) {
      boolean major = deg % 30 == 0;
      double r1 = 90.0;
      double r2 = major ? 80.0 : 85.0;
      double rad = Math.toRadians(deg - 90);
      double x1 = 100.0 + r1 * Math.cos(rad);
      double y1 = 100.0 + r1 * Math.sin(rad);
      double x2 = 100.0 + r2 * Math.cos(rad);
      double y2 = 100.0 + r2 * Math.sin(rad);
      ticks.append(String.format(Locale.US,
          "<line class=\"%s\" x1=\"%.2f\" y1=\"%.2f\" x2=\"%.2f\" y2=\"%.2f\" stroke-width=\"%s\" opacity=\"%s\"/>",
          major ? "tick tick-major" : "tick",
          x1, y1, x2, y2,
          major ? "1.4" : "0.6",
          major ? "0.8" : "0.45"));
    }

    return "<svg viewBox=\"0 0 200 200\">"
        + "<circle class=\"ring-line\" cx=\"100\" cy=\"100\" r=\"96\" stroke-width=\"2\"/>"
        + "<circle class=\"ring-line\" cx=\"100\" cy=\"100\" r=\"90\" stroke-width=\"0.75\"/>"
        + "<circle class=\"ring-line\" cx=\"100\" cy=\"100\" r=\"66\" stroke-width=\"0.75\" opacity=\"0.6\"/>"
        + "<g>" + ticks + "</g>"
        + "<g>"
        + "<polygon class=\"rose-star-minor\" points=\"100,100 108,108 100,38 92,108\" opacity=\"0.55\" transform=\"rotate(45 100 100)\"/>"
        + "<polygon class=\"rose-star-minor\" points=\"100,100 108,108 100,38 92,108\" opacity=\"0.55\" transform=\"rotate(135 100 100)\"/>"
        + "<polygon class=\"rose-star-minor\" points=\"100,100 108,108 100,38 92,108\" opacity=\"0.55\" transform=\"rotate(225 100 100)\"/>"
        + "<polygon class=\"rose-star-minor\" points=\"100,100 108,108 100,38 92,108\" opacity=\"0.55\" transform=\"rotate(315 100 100)\"/>"
        + "<polygon class=\"rose-star-major\" points=\"100,100 110,110 100,26 90,110\"/>"
        + "<polygon class=\"rose-star-major\" points=\"100,100 110,110 100,26 90,110\" transform=\"rotate(90 100 100)\"/>"
        + "<polygon class=\"rose-star-major\" points=\"100,100 110,110 100,26 90,110\" transform=\"rotate(180 100 100)\"/>"
        + "<polygon class=\"rose-star-major\" points=\"100,100 110,110 100,26 90,110\" transform=\"rotate(270 100 100)\"/>"
        + "</g>"
        + "<text class=\"rose-letter\" x=\"100\" y=\"22\" text-anchor=\"middle\" font-size=\"15\">N</text>"
        + "<text class=\"rose-letter\" x=\"183\" y=\"105\" text-anchor=\"middle\" font-size=\"13\">E</text>"
        + "<text class=\"rose-letter\" x=\"100\" y=\"190\" text-anchor=\"middle\" font-size=\"13\">S</text>"
        + "<text class=\"rose-letter\" x=\"17\" y=\"105\" text-anchor=\"middle\" font-size=\"13\">W</text>"
        + "<g class=\"needle-group\">"
        + "<polygon class=\"needle-n\" points=\"100,30 106,100 100,108 94,100\"/>"
        + "<polygon class=\"needle-s\" points=\"100,170 106,100 100,92 94,100\"/>"
        + "</g>"
        + "<circle class=\"rose-center needle-cap\" cx=\"100\" cy=\"100\" r=\"7\" stroke-width=\"1.5\"/>"
        + "<circle cx=\"100\" cy=\"100\" r=\"2.4\" fill=\"var(--gold-500)\"/>"
        + "</svg>";
  }
}
