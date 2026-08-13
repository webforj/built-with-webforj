# Location Tracker — Friends

A small webforJ demo built on the `webforj-archetype-hello-world` archetype
(webforJ 26.02-SNAPSHOT) that exercises **both** the browser Geolocation API
and **all three** faces of the Badge feature in one screen:

- **Browser tab (favicon) badge** — `Page.getCurrent().setIconBadge(count)`
- **Installed app / system tray badge** — `App.setBadge(count)`
- **In-app Badge component** — `bell.setBadge(new Badge(...))`

The screen is a grid of friend cards, one per city. Your browser location
determines your position; each card shows the great-circle distance from you
to that friend (Haversine), and the grid sorts nearest-first. Adding a new
friend makes them arrive "unseen" — that unseen count is what drives all
three badges. Clicking the bell marks everything as read and clears them.

## Run

```bash
mvn
```

Then open [http://localhost:8080](http://localhost:8080). The default goal
is `compile webforj:watch jetty:run`, so the frontend bundle stays in sync as
you edit `src/main/frontend/app.css`.

## Photos

Each `City` enum entry carries a `photoUrl` — a stable Wikimedia Commons CDN
thumbnail sourced once via the Wikipedia REST summary API and hardcoded in
`model/City.java`. All 25 shipped cities have one. `FriendCard` applies the
URL as an inline `background-image` layered over the city's signature CSS
gradient (defined in `app.css`), so:

- Photo loads → user sees the real skyline.
- Photo is slow or 404s → the gradient shows through as a first-class fallback.
- You add a new city with `photoUrl = null` → gradient carries it. Just add
  a matching `.friend-card--<slug> { --lt-card-gradient: ... }` rule.

The URLs are hosted on `upload.wikimedia.org` under CC / public-domain
licenses; they don't move. If you'd rather host photos yourself, replace the
`photoUrl` strings with your own URLs — same shape works.

## Watch the badges

All three badges are wired to the same unseen counter in `FriendsView.syncBadges()`.

### In-app badge (always visible)

The bell in the header carries a red `dwc-badge` slotted via
`button.setBadge(badge)`. This one shows up in the app itself, so it's the
easiest to see in any screenshot.

### Browser tab (favicon) badge

Drawn by `Page.getCurrent().setIconBadge(count)`. Look at the tab strip in
Chrome / Edge / Firefox after adding a friend — the favicon gets a red
circle with the number over the top-right corner. Safari currently ignores
this at the OS level (silently).

Because Playwright screenshots don't include browser chrome, the demo's
screenshot harness (`_upgrade-26.02/screenshot.mjs`) also extracts the
current `<link rel="icon">` data URL and writes it out as a standalone PNG
alongside each shot when `"captureFavicon": true` is set — that's why you'll
see `*.favicon.png` files in the shots folder. Those *are* what the browser
renders in the tab.

### System tray / installed-app badge

`App.setBadge(count)` writes to the OS-level app badge. To see it:

1. Open the app in Chrome or Edge on `localhost` (secure-context requirement
   is satisfied by `localhost`).
2. Browser menu → **Install Friends…** (Chrome shows an install icon in the
   address bar; Edge has one in the toolbar).
3. Launch the installed app from the dock/taskbar. Add a friend.
4. Look at the app icon — you'll see the OS-drawn badge count. On Windows
   it appears as a colored dot with count on the taskbar icon; on macOS as
   a red pill on the dock icon.

If the app isn't installed as a PWA, the `App.setBadge()` call is a no-op —
the browser silently ignores it. That's by design, not a bug.

Click the bell in the header to mark everything as seen; all three badges
zero out simultaneously.

## Files worth reading

- `views/FriendsView.java` — the whole app: header, geolocation, badges
- `components/FriendCard.java` — one card; uses the framework's `Avatar` and
  `Badge` components
- `components/AddFriendDialog.java` — name field + `ChoiceBox` city picker
- `service/FriendsService.java` — in-memory seed + `add` + `markAllSeen`
- `util/Haversine.java` — great-circle distance in km
- `src/main/frontend/app.css` — grid, card layout, city gradients

## Prerequisites

- Java 21+
- Maven 3.9+
