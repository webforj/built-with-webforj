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
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.geolocation.Geolocation;
import com.webforj.locationtracker.components.AddFriendDialog;
import com.webforj.locationtracker.components.FriendCard;
import com.webforj.locationtracker.model.Friend;
import com.webforj.locationtracker.service.FriendsService;
import com.webforj.locationtracker.util.Haversine;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.Comparator;
import java.util.List;

@Route("/")
@FrameTitle("Friends")
public class FriendsView extends Composite<AppLayout> {

  // shared across the JVM — fine for a single-user demo
  private static final FriendsService FRIENDS = new FriendsService();

  private final AppLayout self = getBoundComponent();
  private final Div grid = new Div();
  private final Paragraph youAreHere = new Paragraph("Locating you…");
  private final Badge bellBadge = new Badge("0").setTheme(BadgeTheme.DANGER);
  private final Button bell = new Button(TablerIcon.create("bell"));
  private final IconButton themeToggle = new IconButton(TablerIcon.create("moon"));
  private final AddFriendDialog addDialog = new AddFriendDialog();

  private Double userLatitude = null;
  private Double userLongitude = null;
  private boolean darkTheme = false;

  public FriendsView() {
    self.addClassName("app-shell");
    self.setDrawerPlacement(AppLayout.DrawerPlacement.HIDDEN);

    buildHeader();
    buildContent();

    self.add(addDialog);

    addDialog.onSave((name, city) -> {
      Friend f = FRIENDS.add(name, city);
      refresh();
      syncBadges();
    });

    // one-shot geolocation request; may prompt the user for permission
    if (Geolocation.isPresent()) {
      Geolocation.getCurrent().getCurrentPosition()
          .thenAccept(pos -> {
            userLatitude = pos.getLatitude();
            userLongitude = pos.getLongitude();
            youAreHere.setText(String.format(
                "📍  You are near %.3f, %.3f — distances update below",
                userLatitude, userLongitude));
            refresh();
          })
          .exceptionally(err -> {
            youAreHere.setText("📍  Location permission denied — showing friends without distances");
            return null;
          });
    } else {
      youAreHere.setText("📍  Geolocation unavailable — showing friends without distances");
    }

    refresh();
    syncBadges();
  }

  private void buildHeader() {
    Toolbar toolbar = new Toolbar();

    H1 title = new H1("Friends");
    title.addClassName("app-shell__title");
    toolbar.addToTitle(title);

    bell.setTheme(ButtonTheme.OUTLINED_GRAY);
    bell.setBadge(bellBadge);
    bell.addClassName("app-shell__bell");
    bell.onClick(e -> {
      FRIENDS.markAllSeen();
      refresh();
      syncBadges();
    });

    themeToggle.addClassName("app-shell__theme");
    themeToggle.onClick(e -> toggleTheme());

    Button addBtn = new Button("Add friend");
    addBtn.setTheme(ButtonTheme.PRIMARY);
    addBtn.setPrefixComponent(TablerIcon.create("plus"));
    addBtn.onClick(e -> addDialog.open());

    // Layout: [ title | ... (spacer) ... | Add friend | (spacer) | bell + themeToggle ]
    Div center = new Div(addBtn).addClassName("app-shell__center");
    Div right  = new Div(bell, themeToggle).addClassName("app-shell__right");
    Div actions = new Div(center, right).addClassName("app-shell__actions");
    toolbar.add(actions);

    self.addToHeader(toolbar);
  }

  private void toggleTheme() {
    darkTheme = !darkTheme;
    App.setTheme(darkTheme ? "dark" : "light");
    themeToggle.setName(darkTheme ? "sun" : "moon");
  }

  private void buildContent() {
    Div content = new Div().addClassName("app-shell__content");

    youAreHere.addClassName("app-shell__you");

    grid.addClassName("friends-grid");

    content.add(youAreHere, grid);
    self.add(content);
  }

  private void refresh() {
    grid.removeAll();
    List<Friend> sorted = FRIENDS.getAll().stream()
        .sorted(Comparator.comparingDouble(this::distanceTo))
        .toList();
    for (Friend f : sorted) {
      grid.add(new FriendCard(f, userLatitude, userLongitude));
    }
  }

  private double distanceTo(Friend f) {
    if (userLatitude == null || userLongitude == null) return 0.0;
    return Haversine.kilometersBetween(userLatitude, userLongitude,
        f.getCity().getLatitude(), f.getCity().getLongitude());
  }

  private void syncBadges() {
    int count = FRIENDS.unseenCount();

    // in-app badge — the chip slotted into the bell button
    bellBadge.setText(String.valueOf(count));
    bellBadge.setVisible(count > 0);

    // browser tab (favicon) badge
    if (count > 0) {
      Page.getCurrent().setIconBadge(count);
    } else {
      Page.getCurrent().setIconBadge(0);
    }

    // installed-app / system tray badge
    App.setBadge(count);
  }
}
