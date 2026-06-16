/* ============================================================================
   WONDERS DATASET
   The card template renders entirely from these records. In production this
   array holds ~22 wonders; here we ship the three fully-specified examples
   (Standing structure / Destroyed structure / Natural phenomenon) plus Petra
   so the Ruins status variant can be demonstrated honestly.

   coords are decimal degrees. For zonal/diffuse "wonders" (e.g. the auroral
   oval) coords mark a representative point and `diffuse:true` softens the
   distance copy ("~" prefix, "auroral oval" note).
   ============================================================================ */
window.WONDERS = [
  {
    id: "giza",
    name: "Great Pyramid of Giza",
    era: "ancient",
    status: "standing",
    built: "2570 BCE",
    fell: null,
    coords: { lat: 29.9792, lng: 31.1342 },
    emblem: "pyramid",
    blurb:
      "The only Classical wonder still standing. For 3,800 years the tallest human-made structure on Earth, originally clad in polished limestone that gleamed across the desert.",
    flavor: "Man fears Time, yet Time fears the Pyramids.",
    plate: "Engraving — the Necropolis at Giza, west bank of the Nile",
  },
  {
    id: "alexandria",
    name: "Lighthouse of Alexandria",
    era: "ancient",
    status: "destroyed",
    built: "280 BCE",
    fell: "956–1323 CE",
    coords: { lat: 31.2139, lng: 29.8856 },
    emblem: "lighthouse",
    blurb:
      "A 100-metre beacon of polished bronze, visible 50 km out to sea. It collapsed in stages over four centuries of tremors. Its stones now sit in the foundation of the Citadel of Qaitbay.",
    flavor: "A flame to guide every weary ship home.",
    plate: "Engraving — the Pharos, reconstructed from antique accounts",
  },
  {
    id: "aurora",
    name: "Aurora Borealis",
    era: "natural",
    status: "standing",
    built: "Primordial",
    fell: null,
    coords: { lat: 80.0, lng: -70.0 },
    diffuse: true,
    diffuseNote: "auroral oval",
    emblem: "aurora",
    blurb:
      "Solar wind colliding with the magnetosphere paints the polar sky in ribbons of green and crimson. The only wonder that moves, breathes, and refuses to be photographed truthfully.",
    flavor: "The heavens unfurl their banners for no audience but their own.",
    plate: "Engraving — Northern Lights over the polar circle",
  },

  /* ---- Ruins-state exemplar (used in the Card States gallery) ------------- */
  {
    id: "petra",
    name: "Petra",
    era: "ancient",
    status: "ruins",
    built: "~312 BCE",
    fell: null,
    coords: { lat: 30.3285, lng: 35.4444 },
    emblem: "petra",
    blurb:
      "The rose-red city half as old as time, carved straight into the sandstone cliffs of Jordan. Its Treasury façade still stands; the metropolis behind it has worn back into the rock.",
    flavor: "A rose-red city half as old as time.",
    plate: "Engraving — Al-Khazneh, the Treasury at Petra",
  },
];
