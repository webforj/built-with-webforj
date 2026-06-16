package com.webforjgeolocation.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Nav;
import com.webforj.component.html.elements.NativeButton;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FilterChips extends Composite<Nav> {

  public static final String ALL = "all";

  private final Nav self = getBoundComponent();
  private final Map<String, NativeButton> chips = new LinkedHashMap<>();
  private String current = ALL;
  private Consumer<String> onChange = k -> {
  };

  public FilterChips() {
    self.addClassName("filters");
    self.setAttribute("aria-label", "Filter wonders by era");
  }

  public FilterChips addChip(String key, String label) {
    NativeButton btn = new NativeButton(label);
    btn.addClassName("chip");
    btn.setAttribute("type", "button");
    btn.setAttribute("data-filter", key);
    btn.setAttribute("aria-pressed", key.equals(current) ? "true" : "false");
    btn.onClick(ev -> select(key));
    chips.put(key, btn);
    self.add(btn);
    return this;
  }

  public FilterChips select(String key) {
    if (!chips.containsKey(key)) {
      return this;
    }
    current = key;
    chips.forEach((k, b) -> b.setAttribute("aria-pressed", k.equals(key) ? "true" : "false"));
    onChange.accept(key);
    return this;
  }

  public String getCurrent() {
    return current;
  }

  public FilterChips onChange(Consumer<String> handler) {
    this.onChange = handler != null ? handler : k -> {
    };
    return this;
  }
}
