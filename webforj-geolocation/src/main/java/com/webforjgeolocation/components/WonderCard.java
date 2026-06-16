package com.webforjgeolocation.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Article;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.NativeButton;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforjgeolocation.geo.GeoMath;
import com.webforjgeolocation.geo.Unit;
import com.webforjgeolocation.model.Coordinates;
import com.webforjgeolocation.model.Wonder;
import java.util.Locale;
import java.util.function.Consumer;

public class WonderCard extends Composite<Article> {

  private final Article self = getBoundComponent();
  private final Wonder wonder;

  private final Span distValue = new Span();
  private final Span bearWords = new Span();
  private final Span bearDeg = new Span();
  private final Span selectLabel = new Span();

  private Consumer<Wonder> onSelect = w -> {
  };

  public WonderCard(Wonder wonder) {
    this.wonder = wonder;
    initRoot();
    buildBody();
    self.onClick(ev -> onSelect.accept(wonder));
  }

  private void initRoot() {
    self.addClassName("card");
    self.addClassName("status-" + wonder.status().cssClass());
    self.setAttribute("data-id", wonder.id());
    self.setAttribute("data-lat", String.valueOf(wonder.location().latitude()));
    self.setAttribute("data-lng", String.valueOf(wonder.location().longitude()));
    self.setStyle("--accent", wonder.era().accentVar());
    self.setAttribute("tabindex", "0");
    self.setAttribute("role", "button");
    self.setAttribute("aria-pressed", "false");
    self.setAttribute("aria-label",
        wonder.name() + ", " + wonder.era().label() + ", " + wonder.status().label());
    if (wonder.diffuse()) {
      self.setAttribute("data-diffuse", "1");
    }
  }

  private void buildBody() {
    self.add(corner("corner-tl"), corner("corner-tr"),
        corner("corner-bl"), corner("corner-br"));

    Div plate = new Div();
    plate.addClassName("plate");
    Div emblem = new Div();
    emblem.addClassName("emblem");
    emblem.setHtml(EmblemLoader.svgFor(wonder.id()));
    Div plateFrame = new Div();
    plateFrame.addClassName("plate-frame");
    Div plateCaption = new Div();
    plateCaption.addClassName("plate-caption");
    plateCaption.addClassName("mono");
    plateCaption.setText(wonder.plate());
    plate.add(emblem, plateFrame, plateCaption);

    Div body = new Div();
    body.addClassName("card-body");

    Div meta = new Div();
    meta.addClassName("card-meta");
    meta.add(new EraBadge(wonder.era()), new StatusChip(wonder.status()));

    H3 name = new H3(wonder.name());
    name.addClassName("wonder-name");

    Div dates = new Div();
    dates.addClassName("dates");
    dates.addClassName("mono");
    dates.setText(wonder.fell() != null
        ? wonder.built() + " — fell " + wonder.fell()
        : "Built " + wonder.built());

    Paragraph blurb = new Paragraph(wonder.blurb());
    blurb.addClassName("blurb");

    Paragraph flavor = new Paragraph(wonder.flavor());
    flavor.addClassName("flavor");

    body.add(meta, name, dates, blurb, flavor, buildInstrumentRow());

    Div selectRow = new Div();
    selectRow.addClassName("select-row");
    NativeButton selectBtn = new NativeButton();
    selectBtn.addClassName("select-btn");
    selectBtn.setAttribute("type", "button");
    selectBtn.setAttribute("data-role", "select");
    selectBtn.setAttribute("tabindex", "-1");
    Span needleGlyph = new Span();
    needleGlyph.addClassName("needle-glyph");
    needleGlyph.setText("✧");
    selectLabel.setAttribute("data-role", "select-label");
    selectLabel.setText("Select — swing the compass");
    selectBtn.add(needleGlyph, selectLabel);
    selectRow.add(selectBtn);

    self.add(plate, body, selectRow);
  }

  private Div buildInstrumentRow() {
    Div instr = new Div();
    instr.addClassName("card-instr");

    Div coords = new Div();
    coords.addClassName("coords");
    coords.addClassName("mono");
    coords.setText("⌖ " + formatCoord(wonder.location())
        + (wonder.diffuse() ? "  · " + wonder.diffuseNote() : ""));

    Div distBlock = new Div();
    distBlock.addClassName("dist-block");
    Span distLabel = new Span();
    distLabel.addClassName("dist-label");
    distLabel.setText("Distance");
    distValue.addClassName("dist-value");
    distValue.addClassName("mono");
    distValue.setAttribute("data-role", "dist");
    distBlock.add(distLabel, distValue);

    Div bearBlock = new Div();
    bearBlock.addClassName("bear-block");
    bearWords.addClassName("bear-value");
    bearWords.setAttribute("data-role", "bear-words");
    bearDeg.addClassName("bear-deg");
    bearDeg.addClassName("mono");
    bearDeg.setAttribute("data-role", "bear-deg");
    bearBlock.add(bearWords, bearDeg);

    instr.add(coords, distBlock, bearBlock);
    return instr;
  }

  public WonderCard updateFromOrigin(Coordinates userLocation, Unit unit) {
    double km = GeoMath.haversineKm(userLocation, wonder.location());
    double brg = GeoMath.initialBearingDegrees(userLocation, wonder.location());
    String distStr = GeoMath.formatDistance(km, unit);

    String inner = (wonder.diffuse() ? "<span class=\"approx\">~</span>" : "")
        + distStr + " <span class=\"unit\">" + unit.label() + "</span>";
    distValue.setHtml(inner);
    distValue.setAttribute("aria-label",
        "Distance " + (wonder.diffuse() ? "approximately " : "")
            + distStr + " " + unit.label());

    bearWords.setText(GeoMath.bearingWords(brg));
    bearDeg.setText(Math.round(brg) + "°");
    bearWords.setAttribute("aria-label",
        "Bearing " + GeoMath.bearingWords(brg) + ", " + Math.round(brg) + " degrees");
    return this;
  }

  public WonderCard setSelected(boolean isSelected) {
    if (isSelected) {
      self.addClassName("is-selected");
    } else {
      self.removeClassName("is-selected");
    }
    self.setAttribute("aria-pressed", isSelected ? "true" : "false");
    selectLabel.setText(isSelected ? "Selected" : "Select — swing the compass");
    return this;
  }

  public WonderCard onSelect(Consumer<Wonder> handler) {
    this.onSelect = handler != null ? handler : w -> {
    };
    return this;
  }

  public WonderCard setVisible(boolean visible) {
    self.setStyle("display", visible ? "" : "none");
    return this;
  }

  public Wonder getWonder() {
    return wonder;
  }

  private static Span corner(String position) {
    Span s = new Span();
    s.addClassName("corner");
    s.addClassName(position);
    s.setText("★");
    return s;
  }

  private static String formatCoord(Coordinates c) {
    return String.format(Locale.US, "%.4f°%s, %.4f°%s",
        Math.abs(c.latitude()), c.latitude() >= 0 ? "N" : "S",
        Math.abs(c.longitude()), c.longitude() >= 0 ? "E" : "W");
  }
}
