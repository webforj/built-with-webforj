package com.webforj.builtwithwebforj.components.settings;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;

public class SettingsSection extends Composite<Div> {
  private Div self = getBoundComponent();
  private FlexLayout content;

  public SettingsSection(String title, String description) {
    self.addClassName("settings-view__section");

    H3 sectionTitle = new H3(title);
    sectionTitle.addClassName("settings-view__section-title");

    Paragraph sectionDesc = new Paragraph(description);
    sectionDesc.addClassName("settings-view__section-description");

    content = new FlexLayout();
    content.addClassName("settings-view__section-content");
    content.setDirection(FlexDirection.COLUMN);
    content.setWrap(FlexWrap.WRAP);

    self.add(sectionTitle, sectionDesc, content);
  }

  public void addContent(com.webforj.component.Component... components) {
    content.add(components);
  }

  public void removeAllContent() {
    content.removeAll();
  }
}
