/* ============================================================================
   WONDERS OF THE WORLD — APP LOGIC
   Distance (Haversine) + initial bearing computed live from the user's
   location to each wonder. Selecting a wonder swings the compass needle and
   drives the instrument readout. Units, filters, custom coordinates and the
   info dialog are all wired here.
   ============================================================================ */
(function () {
  "use strict";

  /* ---- Geodesy ----------------------------------------------------------- */
  var R_KM = 6371.0088;
  var toRad = function (d) { return (d * Math.PI) / 180; };
  var toDeg = function (r) { return (r * 180) / Math.PI; };

  function haversineKm(a, b) {
    var dLat = toRad(b.lat - a.lat);
    var dLng = toRad(b.lng - a.lng);
    var la1 = toRad(a.lat), la2 = toRad(b.lat);
    var h = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(la1) * Math.cos(la2) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    return 2 * R_KM * Math.asin(Math.min(1, Math.sqrt(h)));
  }

  function initialBearing(a, b) {
    var la1 = toRad(a.lat), la2 = toRad(b.lat);
    var dLng = toRad(b.lng - a.lng);
    var y = Math.sin(dLng) * Math.cos(la2);
    var x = Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(dLng);
    return (toDeg(Math.atan2(y, x)) + 360) % 360;
  }

  var COMPASS_WORDS = [
    "north", "northeast", "east", "southeast",
    "south", "southwest", "west", "northwest"
  ];
  function bearingWords(deg) {
    return COMPASS_WORDS[Math.round(deg / 45) % 8];
  }

  /* ---- Unit formatting --------------------------------------------------- */
  var UNITS = {
    km: { factor: 1, label: "km" },
    mi: { factor: 0.621371, label: "mi" },
    nm: { factor: 0.539957, label: "nm" },
  };
  function formatDistance(km, unit) {
    var v = km * UNITS[unit].factor;
    var num;
    if (v >= 100) num = Math.round(v).toLocaleString("en-US");
    else if (v >= 10) num = v.toFixed(0);
    else num = v.toFixed(1);
    return { num: num, unit: UNITS[unit].label };
  }

  /* ---- Emblems (primitive-shape engravings; swap for real plates) -------- */
  var EMBLEMS = {
    pyramid:
      '<svg viewBox="0 0 100 100" aria-hidden="true">' +
      '<circle class="faint" cx="74" cy="26" r="9"/>' +
      '<polygon class="ink" points="50,22 86,82 14,82"/>' +
      '<polygon class="faint" points="50,22 50,82 14,82"/>' +
      '<line class="ink-stroke" x1="6" y1="82" x2="94" y2="82"/>' +
      "</svg>",
    lighthouse:
      '<svg viewBox="0 0 100 100" aria-hidden="true">' +
      '<line class="ink-stroke" x1="22" y1="34" x2="6" y2="22" opacity="0.4"/>' +
      '<line class="ink-stroke" x1="78" y1="34" x2="94" y2="22" opacity="0.4"/>' +
      '<rect class="ink" x="44" y="20" width="12" height="12"/>' +
      '<polygon class="ink" points="42,32 58,32 64,84 36,84"/>' +
      '<polygon class="faint" points="50,32 58,32 64,84 50,84"/>' +
      '<line class="ink-stroke" x1="28" y1="86" x2="72" y2="86"/>' +
      "</svg>",
    aurora:
      '<svg viewBox="0 0 100 100" aria-hidden="true">' +
      '<circle class="faint" cx="20" cy="22" r="1.6"/>' +
      '<circle class="faint" cx="80" cy="18" r="1.4"/>' +
      '<circle class="faint" cx="60" cy="14" r="1.2"/>' +
      '<polyline class="ink-stroke" points="24,80 30,44 26,24" opacity="0.85"/>' +
      '<polyline class="ink-stroke" points="40,84 44,40 38,18"/>' +
      '<polyline class="ink-stroke" points="56,84 58,38 54,16"/>' +
      '<polyline class="ink-stroke" points="70,82 76,46 72,26" opacity="0.85"/>' +
      '<line class="ink-stroke" x1="14" y1="88" x2="86" y2="88"/>' +
      "</svg>",
    petra:
      '<svg viewBox="0 0 100 100" aria-hidden="true">' +
      '<polygon class="ink" points="50,16 74,34 26,34"/>' +
      '<rect class="ink" x="30" y="34" width="6" height="50"/>' +
      '<rect class="ink" x="44" y="34" width="6" height="50"/>' +
      '<rect class="ink" x="58" y="34" width="6" height="50"/>' +
      '<rect class="faint" x="46" y="52" width="8" height="32"/>' +
      '<line class="ink-stroke" x1="22" y1="86" x2="78" y2="86"/>' +
      "</svg>",
  };

  var ERA_LABEL = { ancient: "Ancient", medieval: "Medieval", modern: "Modern", natural: "Natural" };
  var ERA_VAR = { ancient: "var(--era-ancient)", medieval: "var(--era-medieval)", modern: "var(--era-modern)", natural: "var(--era-natural)" };
  var STATUS_LABEL = { standing: "Standing", ruins: "Ruins", destroyed: "Destroyed" };

  /* ---- State ------------------------------------------------------------- */
  var state = {
    user: { lat: 41.88, lng: -87.63 },   // Chicago fallback
    accuracy: null,
    unit: "km",
    selectedId: "giza",
    filter: "all",
  };
  var needleRotation = 0; // accumulated degrees for shortest-path swings

  /* ---- Card template ----------------------------------------------------- */
  function fmtCoord(c) {
    var la = Math.abs(c.lat).toFixed(4) + "°" + (c.lat >= 0 ? "N" : "S");
    var ln = Math.abs(c.lng).toFixed(4) + "°" + (c.lng >= 0 ? "E" : "W");
    return la + ", " + ln;
  }

  function cardHTML(w, opts) {
    opts = opts || {};
    var dates = w.fell ? (w.built + " — fell " + w.fell) : ("Built " + w.built);
    var emblem = EMBLEMS[w.emblem] || "";
    var selected = (!opts.gallery && state.selectedId === w.id) ? " is-selected" : "";
    if (opts.forceSelected) selected = " is-selected";
    var statusClass = " status-" + w.status;
    var noHover = opts.gallery ? " no-hover" : "";

    return (
      '<article class="card' + statusClass + selected + noHover + '" ' +
        'data-id="' + w.id + '" data-lat="' + w.coords.lat + '" data-lng="' + w.coords.lng + '" ' +
        (w.diffuse ? 'data-diffuse="1" ' : "") +
        'style="--accent:' + ERA_VAR[w.era] + '" ' +
        (opts.gallery ? "" : 'tabindex="0" role="button" aria-pressed="' + (selected ? "true" : "false") + '" ') +
        'aria-label="' + w.name + ', ' + ERA_LABEL[w.era] + ', ' + STATUS_LABEL[w.status] + '">' +

        '<span class="corner corner-tl">❖</span><span class="corner corner-tr">❖</span>' +
        '<span class="corner corner-bl">❖</span><span class="corner corner-br">❖</span>' +

        '<div class="plate">' +
          '<div class="emblem">' + emblem + '</div>' +
          '<div class="plate-frame"></div>' +
          '<div class="plate-caption mono">' + w.plate + '</div>' +
        '</div>' +

        '<div class="card-body">' +
          '<div class="card-meta">' +
            '<span class="badge">' + ERA_LABEL[w.era] + '</span>' +
            '<span class="chip-status ' + w.status + '"><span class="seal"></span>' + STATUS_LABEL[w.status] + '</span>' +
          '</div>' +
          '<h3 class="wonder-name">' + w.name + '</h3>' +
          '<div class="dates mono">' + dates + '</div>' +
          '<p class="blurb">' + w.blurb + '</p>' +
          '<p class="flavor">' + w.flavor + '</p>' +
          '<div class="card-instr">' +
            '<div class="coords mono">⌖ ' + fmtCoord(w.coords) + (w.diffuse ? '  · ' + w.diffuseNote : "") + '</div>' +
            '<div class="dist-block">' +
              '<span class="dist-label">Distance</span>' +
              '<span class="dist-value mono" data-role="dist"></span>' +
            '</div>' +
            '<div class="bear-block">' +
              '<span class="bear-value" data-role="bear-words"></span>' +
              '<span class="bear-deg mono" data-role="bear-deg"></span>' +
            '</div>' +
          '</div>' +
        '</div>' +

        '<div class="select-row">' +
          '<button class="select-btn" type="button" data-role="select" tabindex="' + (opts.gallery ? "-1" : "0") + '">' +
            '<span class="needle-glyph">✧</span>' +
            '<span data-role="select-label">' + (selected ? "Selected" : "Select — swing the compass") + '</span>' +
          '</button>' +
        '</div>' +
      '</article>'
    );
  }

  /* ---- Per-card instrument values --------------------------------------- */
  function updateCardInstruments(root) {
    var cards = (root || document).querySelectorAll(".card[data-lat]");
    cards.forEach(function (card) {
      var to = { lat: parseFloat(card.dataset.lat), lng: parseFloat(card.dataset.lng) };
      var km = haversineKm(state.user, to);
      var brg = initialBearing(state.user, to);
      var d = formatDistance(km, state.unit);
      var diffuse = card.dataset.diffuse === "1";
      var distEl = card.querySelector('[data-role="dist"]');
      var bwEl = card.querySelector('[data-role="bear-words"]');
      var bdEl = card.querySelector('[data-role="bear-deg"]');
      if (distEl) distEl.innerHTML = (diffuse ? '<span class="approx">~</span>' : "") + d.num + ' <span class="unit">' + d.unit + "</span>";
      if (bwEl) bwEl.textContent = bearingWords(brg);
      if (bdEl) bdEl.textContent = Math.round(brg) + "°";
      // a11y
      if (distEl) distEl.setAttribute("aria-label", "Distance " + (diffuse ? "approximately " : "") + d.num + " " + d.unit);
      if (bwEl) bwEl.setAttribute("aria-label", "Bearing " + bearingWords(brg) + ", " + Math.round(brg) + " degrees");
    });
  }

  /* ---- Compass + readout ------------------------------------------------- */
  function updateReadout() {
    var w = WONDERS.find(function (x) { return x.id === state.selectedId; });
    if (!w) return;
    var km = haversineKm(state.user, w.coords);
    var brg = initialBearing(state.user, w.coords);
    var d = formatDistance(km, state.unit);

    document.getElementById("readout-name").textContent = w.name;
    var distEl = document.getElementById("readout-dist");
    distEl.innerHTML = (w.diffuse ? '<span class="approx">~</span>' : "") + d.num + ' <span class="unit">' + d.unit + "</span>";
    distEl.setAttribute("aria-label", "Distance to " + w.name + ": " + (w.diffuse ? "approximately " : "") + d.num + " " + d.unit);
    document.getElementById("readout-bear-words").textContent = bearingWords(brg);
    var bd = document.getElementById("readout-bear-deg");
    bd.textContent = "· " + Math.round(brg) + "°";
    document.getElementById("readout-bearing").setAttribute("aria-label", "Bearing " + bearingWords(brg) + ", " + Math.round(brg) + " degrees");

    // shortest-path needle swing
    var group = document.getElementById("needle-group");
    if (group) {
      var delta = ((brg - (needleRotation % 360)) + 540) % 360 - 180;
      needleRotation += delta;
      group.style.setProperty("--bearing", needleRotation + "deg");
    }
  }

  /* ---- Selection --------------------------------------------------------- */
  function selectWonder(id) {
    state.selectedId = id;
    document.querySelectorAll("#grid .card").forEach(function (card) {
      var on = card.dataset.id === id;
      card.classList.toggle("is-selected", on);
      card.setAttribute("aria-pressed", on ? "true" : "false");
      var lbl = card.querySelector('[data-role="select-label"]');
      if (lbl) lbl.textContent = on ? "Selected" : "Select — swing the compass";
    });
    updateReadout();
  }

  /* ---- Rendering --------------------------------------------------------- */
  function renderGrid() {
    var grid = document.getElementById("grid");
    var list = WONDERS.filter(function (w) { return w.id !== "petra"; }); // Petra lives in the states gallery
    grid.innerHTML = list.map(function (w) { return cardHTML(w, {}); }).join("");
    wireGrid();
    applyFilter();
    updateCardInstruments(grid);
  }

  function wireGrid() {
    document.querySelectorAll("#grid .card").forEach(function (card) {
      var id = card.dataset.id;
      card.addEventListener("click", function () { selectWonder(id); });
      card.addEventListener("keydown", function (e) {
        if (e.key === "Enter" || e.key === " ") { e.preventDefault(); selectWonder(id); }
      });
    });
  }

  function renderGallery() {
    var byId = {};
    WONDERS.forEach(function (w) { byId[w.id] = w; });
    var cells = [
      { n: "01", label: "Default", w: "giza", opts: {} },
      { n: "02", label: "Hovered", w: "giza", opts: {}, hover: true },
      { n: "03", label: "Selected", w: "giza", opts: { forceSelected: true } },
      { n: "04", label: "Ruins variant", w: "petra", opts: {} },
      { n: "05", label: "Destroyed variant", w: "alexandria", opts: {} },
    ];
    var html = cells.map(function (c) {
      return (
        '<div class="state-cell' + (c.hover ? " frozen-hover" : "") + '">' +
          '<div class="state-label"><span class="n mono">' + c.n + '</span>' + c.label + "</div>" +
          cardHTML(byId[c.w], Object.assign({ gallery: true }, c.opts)) +
        "</div>"
      );
    }).join("");
    var gal = document.getElementById("states-gallery");
    gal.innerHTML = html;
    updateCardInstruments(gal);
  }

  /* ---- Filters ----------------------------------------------------------- */
  function applyFilter() {
    document.querySelectorAll("#grid .card").forEach(function (card) {
      var w = WONDERS.find(function (x) { return x.id === card.dataset.id; });
      var show = state.filter === "all" || (w && w.era === state.filter);
      card.style.display = show ? "" : "none";
    });
  }

  function wireFilters() {
    document.querySelectorAll(".chip").forEach(function (chip) {
      chip.addEventListener("click", function () {
        state.filter = chip.dataset.filter;
        document.querySelectorAll(".chip").forEach(function (c) {
          c.setAttribute("aria-pressed", c === chip ? "true" : "false");
        });
        applyFilter();
      });
    });
  }

  /* ---- Unit toggle ------------------------------------------------------- */
  function wireUnits() {
    document.querySelectorAll(".unit-toggle button").forEach(function (btn) {
      btn.addEventListener("click", function () {
        state.unit = btn.dataset.unit;
        document.querySelectorAll(".unit-toggle button").forEach(function (b) {
          b.setAttribute("aria-pressed", b === btn ? "true" : "false");
        });
        updateCardInstruments();
        updateReadout();
      });
    });
  }

  /* ---- Custom coordinates ----------------------------------------------- */
  function wireCoordsForm() {
    var form = document.getElementById("coords-form");
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      var lat = parseFloat(document.getElementById("in-lat").value);
      var lng = parseFloat(document.getElementById("in-lng").value);
      if (isNaN(lat) || isNaN(lng) || lat < -90 || lat > 90 || lng < -180 || lng > 180) {
        form.classList.add("shake");
        setTimeout(function () { form.classList.remove("shake"); }, 400);
        return;
      }
      state.user = { lat: lat, lng: lng };
      state.accuracy = null;
      setLocationLabel(lat, lng, null, "manual");
      updateCardInstruments();
      updateReadout();
    });
  }

  function setLocationLabel(lat, lng, acc, source) {
    var la = Math.abs(lat).toFixed(2) + "°" + (lat >= 0 ? "N" : "S");
    var ln = Math.abs(lng).toFixed(2) + "°" + (lng >= 0 ? "E" : "W");
    document.getElementById("loc-coords").textContent = la + ", " + ln;
    var accEl = document.getElementById("loc-accuracy");
    if (source === "manual") accEl.textContent = "entered manually";
    else if (source === "fallback") accEl.textContent = "default · enable location to update";
    else accEl.textContent = acc != null ? "±" + Math.round(acc) + "m" : "located";
  }

  /* ---- Info dialog ------------------------------------------------------- */
  function wireDialog() {
    var dlg = document.getElementById("info-dialog");
    var open = document.getElementById("info-btn");
    open.addEventListener("click", function () {
      if (typeof dlg.showModal === "function") dlg.showModal();
      else dlg.setAttribute("open", "");
    });
    dlg.querySelectorAll("[data-close]").forEach(function (b) {
      b.addEventListener("click", function () { dlg.close(); });
    });
    dlg.addEventListener("click", function (e) {
      var sheet = dlg.querySelector(".modal-sheet");
      if (!sheet.contains(e.target)) dlg.close();
    });
  }

  /* ---- Geolocation ------------------------------------------------------- */
  function initGeo() {
    setLocationLabel(state.user.lat, state.user.lng, null, "fallback");
    if (!navigator.geolocation) return;
    navigator.geolocation.getCurrentPosition(
      function (pos) {
        state.user = { lat: pos.coords.latitude, lng: pos.coords.longitude };
        state.accuracy = pos.coords.accuracy;
        setLocationLabel(state.user.lat, state.user.lng, pos.coords.accuracy, "geo");
        updateCardInstruments();
        updateReadout();
      },
      function () { /* keep fallback */ },
      { enableHighAccuracy: false, timeout: 8000, maximumAge: 600000 }
    );
  }

  /* ---- Boot -------------------------------------------------------------- */
  document.addEventListener("DOMContentLoaded", function () {
    renderGrid();
    renderGallery();
    wireFilters();
    wireUnits();
    wireCoordsForm();
    wireDialog();
    initGeo();
    updateReadout();
  });
})();
