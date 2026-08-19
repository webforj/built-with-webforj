package com.webforj.locationtracker.service;

import com.webforj.locationtracker.model.City;
import com.webforj.locationtracker.model.Shipment;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory shipment store. New shipments arrive unseen; the unseen count
 * drives the badges.
 */
public class ShipmentsService {

  private final List<Shipment> shipments = new ArrayList<>();
  private final AtomicInteger counter = new AtomicInteger(482103);

  public ShipmentsService() {
    Instant seedTime = Instant.now().minus(3, ChronoUnit.DAYS);
    shipments.add(new Shipment("SHP-100521", "Nihon Trading Co.",     City.TOKYO,         seedTime, true));
    shipments.add(new Shipment("SHP-100522", "Copacabana Exports",    City.RIO,           seedTime, true));
    shipments.add(new Shipment("SHP-100523", "Cape Coastal Traders",  City.CAPE_TOWN,     seedTime, true));
    shipments.add(new Shipment("SHP-100524", "Norður Logistics",      City.REYKJAVIK,     seedTime, true));
    shipments.add(new Shipment("SHP-100525", "Harbour Freight AU",    City.SYDNEY,        seedTime, true));
    shipments.add(new Shipment("SHP-100526", "Maison Levasseur",      City.PARIS,         seedTime, true));
    shipments.add(new Shipment("SHP-100527", "Pampas Cargo Group",    City.BUENOS_AIRES,  seedTime, true));
    shipments.add(new Shipment("SHP-100528", "Bay Freight Inc.",      City.SAN_FRANCISCO, seedTime, true));
    // unseen, so the badges start at a non-zero count
    shipments.add(0, new Shipment("SHP-482103", "Costa Textiles SL",  City.BARCELONA,     Instant.now(), false));
  }

  public List<Shipment> getAll() {
    return Collections.unmodifiableList(shipments);
  }

  public Shipment add(String consignee, City destination) {
    String trackingId = String.format("SHP-%06d", counter.incrementAndGet());
    Shipment s = new Shipment(trackingId, consignee, destination, Instant.now(), false);
    shipments.add(0, s);
    return s;
  }

  public int unseenCount() {
    return (int) shipments.stream().filter(s -> !s.isSeen()).count();
  }

  public void markAllSeen() {
    shipments.forEach(Shipment::markSeen);
  }
}
