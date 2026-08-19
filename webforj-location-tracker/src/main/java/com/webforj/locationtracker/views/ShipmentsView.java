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

  // static: one shipment list shared by every session in this JVM
  private static final ShipmentsService SHIPMENTS = new ShipmentsService();
  private static final double GEOLOCATION_TIMEOUT_SECONDS = 10;

  private final AppLayout self = getBoundComponent();
  private final Div grid = new Div();
  private final Paragraph hubStatus = new Paragraph();
  private final Badge bellBadge = new Badge("0").setTheme(BadgeTheme.DANGER);
  private final Button bell = new Button(TablerIcon.create("bell"));
  private final IconButton themeToggle = new IconButton(TablerIcon.create("moon"));
  private final NewShipmentDialog createDialog = new NewShipmentDialog();
  private final Button fab = new Button();

  private FlexLayout hubStrip;
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

    // The browser location stands in for the dispatch hub. The app is busy
    // until the request settles, so the cards are sorted before they appear.
    if (Geolocation.isPresent()) {
      App.busy("Locating dispatch hub…");
      Geolocation.getCurrent()
          .useTimeout(GEOLOCATION_TIMEOUT_SECONDS)
          .getCurrentPosition()
          .thenAccept(pos -> {
            hubLatitude = pos.getLatitude();
            hubLongitude = pos.getLongitude();
            showShipments(String.format(
                "Dispatch hub near %.3f, %.3f — distances shown from here",
                hubLatitude, hubLongitude));
          })
          .exceptionally(err -> {
            showShipments("Hub location unavailable — shipments shown without distances");
            return null;
          });
    } else {
      showShipments("Geolocation unavailable — shipments shown without distances");
    }

    syncBadges();
  }

  private void showShipments(String status) {
    hubStatus.setText(status);
    hubStrip.removeClassName("app-shell__you--pending");
    refresh();
    App.busy(false);
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

    // km / mi toggle
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

    // The group renders no element of its own, so the buttons go in the
    // layout and the group only carries the selection logic.
    FlexLayout unitToggle = FlexLayout.create(kmRadio, miRadio)
        .horizontal().align().center().build()
        .setSpacing("var(--dwc-space-xs)")
        .addClassName("app-shell__unit-toggle");

    hubStrip = FlexLayout.create(hubStatus, unitToggle)
        .horizontal().align().center().justify().between().build()
        .setSpacing("var(--dwc-space-m)")
        .addClassName("app-shell__you");

    grid.addClassName("shipments-grid");

    FlexLayout content = FlexLayout.create(hubStrip, grid).vertical().build()
        .setSpacing("var(--dwc-space-l)")
        .addClassName("app-shell__content");
    self.add(content);
    hubStrip.addClassName("app-shell__you--pending");
  }

  private void buildFab() {
    fab.setTheme(ButtonTheme.PRIMARY);
    fab.setPrefixComponent(TablerIcon.create("plus"));
    fab.setAttribute("aria-label", "New shipment");
    fab.setAttribute("title", "New shipment");
    fab.addClassName("app-shell__fab");
    fab.onClick(e -> createDialog.open());

    App.getFrames().get(0).add(fab);
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

    // in-app badge slotted into the bell button
    bellBadge.setText(String.valueOf(count));
    bellBadge.setVisible(count > 0);

    // browser tab (favicon) badge
    Page.getCurrent().setIconBadge(count);

    // installed-app / system tray badge
    App.setBadge(count);
  }
}
