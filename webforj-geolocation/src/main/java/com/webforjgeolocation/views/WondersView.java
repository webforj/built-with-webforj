package com.webforjgeolocation.views;

import com.webforj.component.Composite;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Footer;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Header;
import com.webforj.component.html.elements.Main;
import com.webforj.component.html.elements.NativeButton;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Section;
import com.webforj.component.html.elements.Span;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.geolocation.Geolocation;
import com.webforj.geolocation.event.GeolocationWatchEvent;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforjgeolocation.components.CompassRose;
import com.webforjgeolocation.components.FilterChips;
import com.webforjgeolocation.components.FleuronDivider;
import com.webforjgeolocation.components.UnitToggle;
import com.webforjgeolocation.components.WonderCard;
import com.webforjgeolocation.data.Wonders;
import com.webforjgeolocation.geo.GeoMath;
import com.webforjgeolocation.geo.Unit;
import com.webforjgeolocation.model.Coordinates;
import com.webforjgeolocation.model.Wonder;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Route("/")
@FrameTitle("Wonders of the World")
public class WondersView extends Composite<Main> {

  private final Main self = getBoundComponent();

  private Coordinates userLocation = new Coordinates(41.88, -87.63);
  private Unit currentUnit = Unit.KM;
  private String selectedId = Wonders.ALL.get(0).id();

  private final Map<String, WonderCard> cards = new LinkedHashMap<>();
  private final CompassRose compass = new CompassRose();
  private final UnitToggle unitToggle = new UnitToggle(Unit.KM);

  private final Span locCoords = new Span();
  private final Span locAccuracy = new Span();
  private final Div readoutName = new Div();
  private final Div readoutDist = new Div();
  private final Span readoutBearWords = new Span();
  private final Span readoutBearDeg = new Span();

  private final TextField latInput = new TextField("Latitude");
  private final TextField lngInput = new TextField("Longitude");
  private String latValue = "";
  private String lngValue = "";

  private ListenerRegistration<GeolocationWatchEvent> watchRegistration;

  public WondersView() {
    self.addClassName("page");
    self.add(
        buildMasthead(),
        buildFilters(),
        buildCompassBay(),
        buildCoordsSection(),
        buildCompendiumHeading(),
        buildGrid(),
        buildColophon());

    unitToggle.onChange(this::setUnit);
    refreshAll();
    initGeolocation();
  }

  // ============================ MASTHEAD ============================

  private Header buildMasthead() {
    Header masthead = new Header();
    masthead.addClassName("masthead");
    masthead.setAttribute("data-screen-label", "Masthead");

    NativeButton infoBtn = new NativeButton("?");
    infoBtn.addClassName("info-btn");
    infoBtn.setAttribute("type", "button");
    infoBtn.setAttribute("aria-haspopup", "dialog");
    infoBtn.setAttribute("aria-label", "How to set a custom location");
    // Dialog wiring lives in step 7.

    Div overline = new Div();
    overline.addClassName("overline");
    overline.setText("A Codex of Human & Natural Achievement");

    H1 title = new H1("Wonders of the World");

    Paragraph location = new Paragraph();
    location.addClassName("location");

    Span caps = new Span();
    caps.addClassName("caps");
    caps.setStyle("font-family", "var(--font-mono)");
    caps.setStyle("font-size", "0.72em");
    caps.setStyle("letter-spacing", "0.2em");
    caps.setStyle("color", "var(--text-muted)");
    caps.setText("Your location");

    locCoords.addClassName("mono");
    locCoords.setText("41.88°N, 87.63°W");

    locAccuracy.addClassName("mono");
    locAccuracy.setText("locating…");

    location.add(caps, dot(), locCoords, dot(), locAccuracy);
    masthead.add(infoBtn, overline, title, location);
    return masthead;
  }

  // ============================ FILTER CHIPS ========================

  private FilterChips buildFilters() {
    return new FilterChips()
        .addChip("all", "All")
        .addChip("ancient", "Ancient")
        .addChip("medieval", "Medieval")
        .addChip("modern", "Modern")
        .onChange(this::applyFilter);
  }

  // ============================ COMPASS BAY =========================

  private Section buildCompassBay() {
    Section bay = new Section();
    bay.addClassName("compass-bay");
    bay.addClassName("framed");
    bay.setAttribute("aria-label", "Compass and distance readout");
    bay.setAttribute("data-screen-label", "Compass");

    bay.add(
        corner("corner-tl"), corner("corner-tr"),
        corner("corner-bl"), corner("corner-br"),
        compass,
        buildReadout());
    return bay;
  }

  private Div buildReadout() {
    Div readout = new Div();
    readout.addClassName("readout");

    Div targetLabel = new Div();
    targetLabel.addClassName("target-label");
    targetLabel.setText("Bearing toward");

    readoutName.addClassName("target-name");
    readoutName.setText("—");

    readoutDist.addClassName("distance");
    readoutDist.addClassName("mono");
    readoutDist.setAttribute("aria-live", "polite");
    readoutDist.setText("—");

    Div bearingLine = new Div();
    bearingLine.addClassName("bearing-line");

    readoutBearWords.addClassName("bearing-words");
    readoutBearWords.addClassName("italic");
    readoutBearWords.setText("—");

    readoutBearDeg.addClassName("bearing-deg");
    readoutBearDeg.addClassName("mono");

    bearingLine.add(readoutBearWords, readoutBearDeg);

    Div instrumentRow = new Div();
    instrumentRow.addClassName("instrument-row");

    Span caption = new Span();
    caption.addClassName("mono");
    caption.setStyle("font-size", "var(--t-mono-sm)");
    caption.setStyle("color", "var(--text-muted)");
    caption.setStyle("letter-spacing", "0.12em");
    caption.setText("kilometres · miles · nautical");

    instrumentRow.add(unitToggle, caption);

    readout.add(targetLabel, readoutName, readoutDist, bearingLine, instrumentRow);
    return readout;
  }

  // ============================ COORDS FORM =========================

  private Section buildCoordsSection() {
    Section section = new Section();
    section.addClassName("coords-section");
    section.setAttribute("aria-label", "Measure to custom coordinates");

    Span lede = new Span();
    lede.addClassName("lede");
    lede.setText("Measure to anywhere");

    Div form = new Div();
    form.addClassName("coords-form");

    // Start with Giza coordinates so the form is usable on first click —
    // pressing "Set Origin" without changing anything jumps the user's
    // origin to the Great Pyramid, which is a nice demo moment (Giza shows
    // 0 km, every other wonder gets a fresh great-circle distance).
    latInput.setValue("29.9792");
    lngInput.setValue("31.1342");
    latValue = "29.9792";
    lngValue = "31.1342";
    latInput.setPlaceholder("29.9792");
    lngInput.setPlaceholder("31.1342");
    // Track values as the user types so getValue isn't stale at click time
    // (TextField only syncs on blur by default; this keeps us current per
    // keystroke once the user starts editing).
    latInput.onValueChange(e -> latValue = e.getValue());
    lngInput.onValueChange(e -> lngValue = e.getValue());

    NativeButton submit = new NativeButton("Set Origin");
    submit.addClassName("btn");
    submit.setAttribute("type", "button");
    submit.onClick(ev -> applyCustomOrigin());

    Paragraph hint = new Paragraph(
        "Enter any latitude / longitude to reckon every distance from that point.");
    hint.addClassName("hint");

    form.add(latInput, lngInput, submit, hint);
    section.add(lede, form);
    return section;
  }

  // ============================ COMPENDIUM HEAD =====================

  private Div buildCompendiumHeading() {
    Div head = new Div();
    head.addClassName("section-head");

    Div kicker = new Div();
    kicker.addClassName("kicker");
    kicker.setText("The Compendium");

    H2 title = new H2("Great Works of the Ancient, Medieval & Modern World");

    head.add(kicker, title, new FleuronDivider());
    return head;
  }

  // ============================ GRID ================================

  private Section buildGrid() {
    Section grid = new Section();
    grid.addClassName("grid");
    grid.setAttribute("aria-label", "Wonder cards");

    for (Wonder w : Wonders.ALL) {
      WonderCard card = new WonderCard(w);
      card.onSelect(this::selectWonder);
      if (w.id().equals(selectedId)) {
        card.setSelected(true);
      }
      cards.put(w.id(), card);
      grid.add(card);
    }
    return grid;
  }

  // ============================ COLOPHON ============================

  private Footer buildColophon() {
    Footer footer = new Footer();
    footer.addClassName("colophon");
    footer.add(new FleuronDivider(FleuronDivider.SPARKLE));
    Paragraph line = new Paragraph(
        "Reckoned by Haversine over a spherical Earth · bearings are initial great-circle headings");
    footer.add(line);
    return footer;
  }

  // ============================ INTERACTIONS ========================

  private void selectWonder(Wonder w) {
    selectedId = w.id();
    cards.forEach((id, card) -> card.setSelected(id.equals(selectedId)));
    updateReadout();
  }

  private void applyFilter(String filterKey) {
    cards.forEach((id, card) -> {
      Wonder w = Wonders.findById(id).orElseThrow();
      boolean show = "all".equals(filterKey) || w.era().cssClass().equals(filterKey);
      card.setVisible(show);
    });
  }

  private void setUnit(Unit unit) {
    this.currentUnit = unit;
    refreshAll();
  }

  private void applyCustomOrigin() {
    String latStr = !latValue.isBlank() ? latValue : latInput.getValue();
    String lngStr = !lngValue.isBlank() ? lngValue : lngInput.getValue();
    if (latStr == null || lngStr == null) {
      return;
    }
    try {
      double lat = Double.parseDouble(latStr.trim());
      double lng = Double.parseDouble(lngStr.trim());
      if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
        return;
      }
      userLocation = new Coordinates(lat, lng);
      setLocationLabel(lat, lng, null, "manual");
      refreshAll();
    } catch (NumberFormatException ignored) {
      // Invalid input; ignore for now.
    }
  }

  // ============================ UPDATES =============================

  private void refreshAll() {
    cards.values().forEach(card -> card.updateFromOrigin(userLocation, currentUnit));
    updateReadout();
  }

  private void updateReadout() {
    Wonder w = Wonders.findById(selectedId).orElse(Wonders.ALL.get(0));
    double km = GeoMath.haversineKm(userLocation, w.location());
    double brg = GeoMath.initialBearingDegrees(userLocation, w.location());
    String distStr = GeoMath.formatDistance(km, currentUnit);

    readoutName.setText(w.name());
    String inner = (w.diffuse() ? "<span class=\"approx\">~</span>" : "")
        + distStr + " <span class=\"unit\">" + currentUnit.label() + "</span>";
    readoutDist.setHtml(inner);
    readoutBearWords.setText(GeoMath.bearingWords(brg));
    readoutBearDeg.setText(" · " + Math.round(brg) + "°");
    compass.setBearing(brg);
  }

  private void setLocationLabel(double lat, double lng, Double accuracy, String source) {
    String la = String.format(Locale.US, "%.2f°%s",
        Math.abs(lat), lat >= 0 ? "N" : "S");
    String ln = String.format(Locale.US, "%.2f°%s",
        Math.abs(lng), lng >= 0 ? "E" : "W");
    locCoords.setText(la + ", " + ln);

    if ("manual".equals(source)) {
      locAccuracy.setText("entered manually");
    } else if ("fallback".equals(source)) {
      locAccuracy.setText("default · enable location to update");
    } else if (accuracy != null) {
      locAccuracy.setText("±" + Math.round(accuracy) + "m");
    } else {
      locAccuracy.setText("located");
    }
  }

  // ============================ GEOLOCATION =========================

  private void initGeolocation() {
    setLocationLabel(userLocation.latitude(), userLocation.longitude(), null, "fallback");
    if (!Geolocation.isPresent()) {
      return;
    }
    Geolocation geo = Geolocation.getCurrent();

    geo.getCurrentPosition()
        .thenAccept(pos -> {
          userLocation = new Coordinates(pos.getLatitude(), pos.getLongitude());
          setLocationLabel(pos.getLatitude(), pos.getLongitude(), pos.getAccuracy(), "geo");
          refreshAll();
        })
        .exceptionally(t -> null);

    watchRegistration = geo.onWatch(this::onWatchEvent);
  }

  private void onWatchEvent(GeolocationWatchEvent event) {
    if (!event.isSuccess()) {
      return;
    }
    event.getPosition().ifPresent(pos -> {
      userLocation = new Coordinates(pos.getLatitude(), pos.getLongitude());
      setLocationLabel(pos.getLatitude(), pos.getLongitude(), pos.getAccuracy(), "geo");
      refreshAll();
    });
  }

  @Override
  protected void onDidDestroy() {
    if (watchRegistration != null) {
      watchRegistration.remove();
    }
  }

  // ============================ SMALL HELPERS =======================

  private static Span dot() {
    Span s = new Span();
    s.addClassName("dot");
    s.setText("·");
    return s;
  }

  private static Span corner(String position) {
    Span s = new Span();
    s.addClassName("corner");
    s.addClassName(position);
    s.setText("★");
    return s;
  }
}
