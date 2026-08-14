package com.webforj.locationtracker.model;

import java.time.Instant;
import java.util.UUID;

/**
 * One shipment in transit to a destination city. Carries a human-friendly
 * tracking ID (SHP-######) and the consignee name; the destination is a
 * {@link City} so we can compute great-circle distance from the dispatch hub.
 */
public class Shipment {
  private final String id;
  private final String trackingId;
  private final String consignee;
  private final City destination;
  private final Instant createdAt;
  private boolean seen;

  public Shipment(String trackingId, String consignee, City destination,
                  Instant createdAt, boolean seen) {
    this.id = UUID.randomUUID().toString();
    this.trackingId = trackingId;
    this.consignee = consignee;
    this.destination = destination;
    this.createdAt = createdAt;
    this.seen = seen;
  }

  public String getId() { return id; }
  public String getTrackingId() { return trackingId; }
  public String getConsignee() { return consignee; }
  public City getDestination() { return destination; }
  public Instant getCreatedAt() { return createdAt; }
  public boolean isSeen() { return seen; }
  public void markSeen() { this.seen = true; }

  public String consigneeInitials() {
    String[] parts = consignee.trim().split("\\s+");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < parts.length && sb.length() < 2; i++) {
      if (!parts[i].isEmpty()) sb.append(Character.toUpperCase(parts[i].charAt(0)));
    }
    return sb.length() == 0 ? "?" : sb.toString();
  }
}
