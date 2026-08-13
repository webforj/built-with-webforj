package com.webforj.locationtracker.model;

import java.time.Instant;
import java.util.UUID;

public class Friend {
  private final String id;
  private final String name;
  private final City city;
  private final Instant addedAt;
  private boolean seen;

  public Friend(String name, City city, Instant addedAt, boolean seen) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.city = city;
    this.addedAt = addedAt;
    this.seen = seen;
  }

  public String getId() { return id; }
  public String getName() { return name; }
  public City getCity() { return city; }
  public Instant getAddedAt() { return addedAt; }
  public boolean isSeen() { return seen; }
  public void markSeen() { this.seen = true; }

  public String initials() {
    String[] parts = name.trim().split("\\s+");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < parts.length && sb.length() < 2; i++) {
      if (!parts[i].isEmpty()) sb.append(Character.toUpperCase(parts[i].charAt(0)));
    }
    return sb.length() == 0 ? "?" : sb.toString();
  }
}
