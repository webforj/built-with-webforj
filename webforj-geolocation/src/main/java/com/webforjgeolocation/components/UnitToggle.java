package com.webforjgeolocation.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.NativeButton;
import com.webforjgeolocation.geo.Unit;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class UnitToggle extends Composite<Div> {

  private final Div self = getBoundComponent();
  private final Map<Unit, NativeButton> buttons = new EnumMap<>(Unit.class);
  private Unit current;
  private Consumer<Unit> onChange = u -> {
  };

  public UnitToggle() {
    this(Unit.KM);
  }

  public UnitToggle(Unit initial) {
    self.addClassName("unit-toggle");
    self.setAttribute("role", "group");
    self.setAttribute("aria-label", "Distance units");

    for (Unit u : Unit.values()) {
      NativeButton b = new NativeButton(u.label());
      b.setAttribute("type", "button");
      b.setAttribute("data-unit", u.name().toLowerCase());
      b.setAttribute("aria-pressed", "false");
      b.onClick(ev -> select(u));
      buttons.put(u, b);
      self.add(b);
    }
    select(initial);
  }

  public UnitToggle select(Unit u) {
    current = u;
    buttons.forEach((unit, btn) -> btn.setAttribute("aria-pressed", unit == u ? "true" : "false"));
    onChange.accept(u);
    return this;
  }

  public Unit getUnit() {
    return current;
  }

  public UnitToggle onChange(Consumer<Unit> handler) {
    this.onChange = handler != null ? handler : u -> {
    };
    return this;
  }
}
