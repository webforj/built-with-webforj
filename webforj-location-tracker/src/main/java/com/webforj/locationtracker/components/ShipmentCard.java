package com.webforj.locationtracker.components;

import com.webforj.component.Composite;
import com.webforj.component.avatar.Avatar;
import com.webforj.component.avatar.AvatarExpanse;
import com.webforj.component.avatar.AvatarTheme;
import com.webforj.component.badge.Badge;
import com.webforj.component.badge.BadgeTheme;
import com.webforj.component.card.Card;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Img;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.locationtracker.model.City;
import com.webforj.locationtracker.model.Shipment;
import com.webforj.locationtracker.util.Haversine;

/**
 * A shipment rendered as a {@code dwc-card}:
 *
 * <ul>
 *   <li><b>figure</b> — hero photo of the destination city</li>
 *   <li><b>title</b> — destination city + country</li>
 *   <li><b>caption</b> — tracking ID</li>
 *   <li><b>header-actions</b> — "NEW" badge for unseen shipments</li>
 *   <li><b>body</b> — consignee avatar + name</li>
 *   <li><b>footer</b> — great-circle distance from the dispatch hub</li>
 * </ul>
 */
public class ShipmentCard extends Composite<Card> {

  private final Card self = getBoundComponent();

  public ShipmentCard(Shipment shipment, Double hubLatitude, Double hubLongitude, boolean useMiles) {
    City dest = shipment.getDestination();

    self.addClassName("shipment-card");
    self.setShadow(Card.Shadow.SMALL);

    // figure ------------------------------------------------------------
    // The wrapper is painted behind the img, so it stands in while the photo
    // loads and if the URL is missing or fails.
    String photoUrl = dest.getPhotoUrl();
    Div heroWrap = new Div().addClassName("shipment-card__hero");
    if (photoUrl != null && !photoUrl.isBlank()) {
      Img photo = new Img(photoUrl);
      photo.setAttribute("alt", dest.getLabel() + " skyline");
      photo.addClassName("shipment-card__photo");
      heroWrap.add(photo);
    }
    self.addToFigure(heroWrap);

    // title + caption ---------------------------------------------------
    self.addToTitle(new Span(dest.getLabel() + ", " + dest.getCountry()));
    self.addToCaption(new Span(shipment.getTrackingId()));

    // header actions: NEW chip for unseen shipments ---------------------
    if (!shipment.isSeen()) {
      Badge chip = new Badge("NEW").setTheme(BadgeTheme.DANGER);
      chip.addClassName("shipment-card__new");
      self.addToHeaderActions(chip);
    }

    // body: consignee avatar + name ------------------------------------
    Avatar avatar = new Avatar(shipment.getConsignee(), shipment.consigneeInitials())
        .setTheme(AvatarTheme.PRIMARY)
        .setExpanse(AvatarExpanse.MEDIUM);
    avatar.addClassName("shipment-card__avatar");

    Span consigneeLine = new Span(shipment.getConsignee());
    consigneeLine.addClassName("shipment-card__consignee");

    FlexLayout consigneeRow = FlexLayout.create(avatar, consigneeLine)
        .horizontal().align().center().build()
        .setSpacing("var(--dwc-space-s)");
    self.addToBody(consigneeRow);

    // footer: distance from dispatch hub -------------------------------
    Span distance = new Span();
    distance.addClassName("shipment-card__distance");
    if (hubLatitude != null && hubLongitude != null) {
      double km = Haversine.kilometersBetween(hubLatitude, hubLongitude,
          dest.getLatitude(), dest.getLongitude());
      distance.setText(Haversine.formatDistance(km, useMiles) + " from hub");
    } else {
      distance.setText("waiting on dispatch hub location…");
      distance.addClassName("shipment-card__distance--muted");
    }
    self.addToFooter(distance);
  }
}
