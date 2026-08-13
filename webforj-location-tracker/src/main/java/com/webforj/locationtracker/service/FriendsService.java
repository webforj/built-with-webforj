package com.webforj.locationtracker.service;

import com.webforj.locationtracker.model.City;
import com.webforj.locationtracker.model.Friend;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory friend store. Seeded with a handful of pre-existing (already
 * "seen") friends so the app has something to look at on first load. Users add
 * more via the Add Friend dialog and those arrive unseen, driving the badges.
 */
public class FriendsService {

  private final List<Friend> friends = new ArrayList<>();

  public FriendsService() {
    Instant seedTime = Instant.now().minus(3, ChronoUnit.DAYS);
    friends.add(new Friend("Ada Chen",       City.TOKYO,         seedTime, true));
    friends.add(new Friend("Marcus Silva",   City.RIO,           seedTime, true));
    friends.add(new Friend("Priya Nair",     City.CAPE_TOWN,     seedTime, true));
    friends.add(new Friend("Lena Björk",     City.REYKJAVIK,     seedTime, true));
    friends.add(new Friend("James O'Connor", City.SYDNEY,        seedTime, true));
    friends.add(new Friend("Camille Roux",   City.PARIS,         seedTime, true));
    friends.add(new Friend("Diego Fernández", City.BUENOS_AIRES, seedTime, true));
    friends.add(new Friend("Yuki Tanaka",    City.SAN_FRANCISCO, seedTime, true));
    // One "new" friend so the notification badges are visible from load
    friends.add(0, new Friend("Sofia Reyes",  City.BARCELONA,    Instant.now(), false));
  }

  public List<Friend> getAll() {
    return Collections.unmodifiableList(friends);
  }

  public Friend add(String name, City city) {
    Friend f = new Friend(name, city, Instant.now(), false);
    friends.add(0, f);
    return f;
  }

  public int unseenCount() {
    return (int) friends.stream().filter(f -> !f.isSeen()).count();
  }

  public void markAllSeen() {
    friends.forEach(Friend::markSeen);
  }
}
