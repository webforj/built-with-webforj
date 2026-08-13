package com.webforj.locationtracker.components;

import com.webforj.component.Composite;
import com.webforj.component.avatar.Avatar;
import com.webforj.component.avatar.AvatarExpanse;
import com.webforj.component.avatar.AvatarTheme;
import com.webforj.component.badge.Badge;
import com.webforj.component.badge.BadgeTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.locationtracker.model.Friend;
import com.webforj.locationtracker.util.Haversine;

/**
 * One card in the friends grid. The whole card is a photo/gradient hero;
 * the avatar + name + city sit bottom-left over a dark bottom-to-top scrim,
 * with the distance chip pinned bottom-right and the NEW indicator top-right.
 */
public class FriendCard extends Composite<Div> {

  private final Div self = getBoundComponent();
  private final Friend friend;

  public FriendCard(Friend friend, Double userLatitude, Double userLongitude) {
    this.friend = friend;

    self.addClassName("friend-card");
    self.addClassName("friend-card--" + friend.getCity().getSlug());

    // hero photo layered over the CSS gradient fallback (from app.css)
    String photoUrl = friend.getCity().getPhotoUrl();
    if (photoUrl != null && !photoUrl.isBlank()) {
      self.setStyle("background-image",
          "url('" + photoUrl.replace("'", "%27") + "'), var(--lt-card-gradient, none)");
    }

    // dark scrim, heavier at the bottom so the text reads
    self.add(new Div().addClassName("friend-card__scrim"));

    // NEW chip — top-right
    if (!friend.isSeen()) {
      Badge chip = new Badge("NEW").setTheme(BadgeTheme.PRIMARY);
      chip.addClassName("friend-card__new");
      self.add(chip);
    }

    // Distance chip — bottom-right (or waiting text if geolocation pending)
    Div distanceChip = new Div().addClassName("friend-card__distance");
    if (userLatitude != null && userLongitude != null) {
      double km = Haversine.kilometersBetween(userLatitude, userLongitude,
          friend.getCity().getLatitude(), friend.getCity().getLongitude());
      distanceChip.setText(Haversine.formatKm(km) + " away");
    } else {
      distanceChip.setText("locating…");
      distanceChip.addClassName("friend-card__distance--muted");
    }
    self.add(distanceChip);

    // Bottom-left info: avatar + name + city
    Div info = new Div().addClassName("friend-card__info");

    Avatar avatar = new Avatar(friend.getName(), friend.initials())
        .setTheme(AvatarTheme.PRIMARY)
        .setExpanse(AvatarExpanse.LARGE);
    avatar.addClassName("friend-card__avatar");

    Div text = new Div().addClassName("friend-card__text");
    Div name = new Div(friend.getName()).addClassName("friend-card__name");
    Paragraph where = new Paragraph(friend.getCity().getFlag() + "  " + friend.getCity().getLabel()
        + ", " + friend.getCity().getCountry());
    where.addClassName("friend-card__where");
    text.add(name, where);

    info.add(avatar, text);
    self.add(info);
  }

  public Friend getFriend() {
    return friend;
  }
}
