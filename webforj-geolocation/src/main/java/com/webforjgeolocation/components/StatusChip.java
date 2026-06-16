package com.webforjgeolocation.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Span;
import com.webforjgeolocation.model.Status;

public class StatusChip extends Composite<Span> {

  private final Span self = getBoundComponent();
  private final Span seal = new Span();
  private final Span label = new Span();
  private Status status;

  public StatusChip(Status status) {
    self.addClassName("chip-status");
    seal.addClassName("seal");
    self.add(seal, label);
    setStatus(status);
  }

  public StatusChip setStatus(Status newStatus) {
    if (this.status != null) {
      self.removeClassName(this.status.cssClass());
    }
    this.status = newStatus;
    self.addClassName(newStatus.cssClass());
    label.setText(newStatus.label());
    return this;
  }

  public Status getStatus() {
    return status;
  }
}
