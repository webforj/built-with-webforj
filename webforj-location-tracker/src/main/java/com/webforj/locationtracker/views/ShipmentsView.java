package com.webforj.locationtracker.views;

import com.webforj.App;
import com.webforj.Page;
import com.webforj.component.Composite;
import com.webforj.component.badge.Badge;
import com.webforj.component.badge.BadgeTheme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.icons.IconButton;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.optioninput.RadioButton;
import com.webforj.component.optioninput.RadioButtonGroup;
import com.webforj.geolocation.Geolocation;
import com.webforj.locationtracker.components.NewShipmentDialog;
import com.webforj.locationtracker.components.ShipmentCard;
import com.webforj.locationtracker.model.Shipment;
import com.webforj.locationtracker.service.ShipmentsService;
import com.webforj.locationtracker.util.Haversine;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.Comparator;
import java.util.List;

@Route("/")
@FrameTitle("Shipments")
public class ShipmentsView extends Composite<AppLayout> {

  // shared across the JVM — fine for a single-user demo
  private static final ShipmentsService SHIPMENTS = new ShipmentsService();

  private final AppLayout self = getBoundComponent();
  private final Div grid = new Div();
  private final Paragraph hubStatus = new Paragraph("Locating dispatch hub…");
  private final Badge bellBadge = new Badge("0").setTheme(BadgeTheme.DANGER);
  private final Button bell = new Button(TablerIcon.create("bell"));
  private final IconButton themeToggle = new IconButton(TablerIcon.create("moon"));
  private final NewShipmentDialog createDialog = new NewShipmentDialog();

  private Double hubLatitude = null;
  private Double hubLongitude = null;
  private boolean darkTheme = false;
  private boolean useMiles = false;

  public ShipmentsView() {
    self.addClassName("app-shell");
    self.setDrawerPlacement(AppLayout.DrawerPlacement.HIDDEN);

    buildHeader();
    buildContent();
    buildFab();

    self.add(createDialog);

    createDialog.onSave((consignee, destination) -> {
      SHIPMENTS.add(consignee, destination);
      refresh();
      syncBadges();
    });

    // one-shot geolocation request; treated as the dispatch hub's location
    if (Geolocation.isPresent()) {
      Geolocation.getCurrent().getCurrentPosition()
          .thenAccept(pos -> {
            hubLatitude = pos.getLatitude();
            hubLongitude = pos.getLongitude();
            hubStatus.setText(String.format(
                "🏭  Dispatch hub near %.3f, %.3f — distances shown from here",
                hubLatitude, hubLongitude));
            refresh();
          })
          .exceptionally(err -> {
            hubStatus.setText("🏭  Hub location unavailable — shipments shown without distances");
            return null;
          });
    } else {
      hubStatus.setText("🏭  Geolocation unavailable — shipments shown without distances");
    }

    refresh();
    syncBadges();
  }

  private void buildHeader() {
    Toolbar toolbar = new Toolbar();

    H1 title = new H1("Shipments");
    title.addClassName("app-shell__title");
    FlexLayout brand = FlexLayout.create(TablerIcon.create("truck-delivery"), title)
        .horizontal().align().center().build()
        .setSpacing("var(--dwc-space-s)")
        .addClassName("app-shell__brand");
    toolbar.addToTitle(brand);

    bell.setTheme(ButtonTheme.OUTLINED_GRAY);
    bell.setBadge(bellBadge);
    bell.addClassName("app-shell__bell");
    bell.onClick(e -> {
      SHIPMENTS.markAllSeen();
      refresh();
      syncBadges();
    });

    themeToggle.addClassName("app-shell__theme");
    themeToggle.onClick(e -> toggleTheme());

    // Toolbar: [ title ] .............................. [ bell + themeToggle ]
    FlexLayout actions = FlexLayout.create(bell, themeToggle)
        .horizontal().align().center().justify().end().build()
        .setSpacing("var(--dwc-space-s)")
        .setStyle("flex", "1");
    toolbar.add(actions);

    self.addToHeader(toolbar);
  }

  private void toggleTheme() {
    darkTheme = !darkTheme;
    App.setTheme(darkTheme ? "dark" : "light");
    themeToggle.setName(darkTheme ? "sun" : "moon");
  }

  private void buildContent() {
    hubStatus.addClassName("app-shell__you-text");

    // km / mi toggle — plain grouped RadioButtons
    RadioButton kmRadio = new RadioButton("km", "km", true);
    RadioButton miRadio = new RadioButton("mi", "mi", false);
    RadioButtonGroup unitGroup = new RadioButtonGroup("distanceUnit", kmRadio, miRadio);
    unitGroup.onChange(e -> {
      RadioButton checked = e.getChecked();
      if (checked != null) {
        useMiles = "mi".equals(checked.getName());
        refresh();
      }
    });

    FlexLayout unitToggle = FlexLayout.create(unitGroup)
        .horizontal().align().center().build()
        .setSpacing("var(--dwc-space-xs)")
        .addClassName("app-shell__unit-toggle");

    // Strip: "Dispatch hub near…" on the left, km/mi toggle on the right
    FlexLayout hubStrip = FlexLayout.create(hubStatus, unitToggle)
        .horizontal().align().center().justify().between().build()
        .setSpacing("var(--dwc-space-m)")
        .addClassName("app-shell__you");

    grid.addClassName("shipments-grid");

    FlexLayout content = FlexLayout.create(hubStrip, grid).vertical().build()
        .setSpacing("var(--dwc-space-l)")
        .addClassName("app-shell__content");
    self.add(content);
  }

  private void buildFab() {
    Button fab = new Button();
    fab.setTheme(ButtonTheme.PRIMARY);
    fab.setPrefixComponent(TablerIcon.create("plus"));
    fab.setAttribute("aria-label", "New shipment");
    fab.setAttribute("title", "New shipment");
    fab.addClassName("app-shell__fab");
    fab.onClick(e -> createDialog.open());
    self.add(fab);

    // Reparent the FAB to <body> so `position: fixed` anchors to the
    // viewport rather than AppLayout's internal scroll container (which
    // becomes a fixed-position containing block on some browsers, causing
    // the FAB to drift with content). Poll briefly in case the element
    // isn't mounted by the time this JS runs.
    Page.getCurrent().executeJsAsync(
        "(function move(tries){"
            + "  var el = document.querySelector('.app-shell__fab');"
            + "  if (el) {"
            + "    if (el.parentNode !== document.body) document.body.appendChild(el);"
            + "    return;"
            + "  }"
            + "  if (tries > 0) setTimeout(function(){ move(tries - 1); }, 100);"
            + "})(20);");
  }

  private void refresh() {
    grid.removeAll();
    List<Shipment> sorted = SHIPMENTS.getAll().stream()
        .sorted(Comparator.comparingDouble(this::distanceTo))
        .toList();
    for (Shipment s : sorted) {
      grid.add(new ShipmentCard(s, hubLatitude, hubLongitude, useMiles));
    }
  }

  private double distanceTo(Shipment s) {
    if (hubLatitude == null || hubLongitude == null) return 0.0;
    return Haversine.kilometersBetween(hubLatitude, hubLongitude,
        s.getDestination().getLatitude(), s.getDestination().getLongitude());
  }

  private void syncBadges() {
    int count = SHIPMENTS.unseenCount();

    // in-app badge — the chip slotted into the bell button
    bellBadge.setText(String.valueOf(count));
    bellBadge.setVisible(count > 0);

    // browser tab (favicon) badge
    Page.getCurrent().setIconBadge(count);

    // installed-app / system tray badge
    App.setBadge(count);
  }
}
