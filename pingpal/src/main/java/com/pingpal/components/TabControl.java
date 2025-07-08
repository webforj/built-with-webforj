package com.pingpal.components;

import java.util.LinkedHashMap;
import java.util.UUID;

import com.webforj.component.Composite;
import com.webforj.component.element.event.ElementClickEvent;
import com.webforj.component.html.elements.Div;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.text.Label;

public class TabControl extends Composite<Div> {

    private Div self = getBoundComponent();
    private FlexLayout tabsContainer;
    private Div extraContainer, container;
    private Div activeTab;
    private LinkedHashMap<String, Div> components = new LinkedHashMap<String, Div>();

    public TabControl() {
        self.addClassName("tab-control");

        FlexLayout toolbar = new FlexLayout().addClassName("tab-control-toolbar");
        toolbar.setAlignment(FlexAlignment.CENTER);
        toolbar.setJustifyContent(FlexJustifyContent.BETWEEN);

        tabsContainer = new FlexLayout().addClassName("tab-control-tabs-container");
        extraContainer = new Div().addClassName("tab-control-extra-container");
        toolbar.add(tabsContainer, extraContainer);

        container = new Div().addClassName("tab-control-content-container");
        self.add(toolbar, container);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        for (Div component : components.values()) {
            component.setVisible(false);
        }

        components.entrySet().stream().findFirst().get().getValue().setVisible(true);
    }

    public void addTab(String title, Div component) {
        String uuid = UUID.randomUUID().toString();
        components.put(uuid, component);

        Div tab = new Div().addClassName("tab-control-tab");
        tab.setUserData("UUID", uuid);

        Label label = new Label(title).addClassName("tab-control-tab-label");
        tab.add(label);
        tabsContainer.add(tab);

        tab.onClick(this::onTabSelect);

        if (components.size() == 1) {
            tab.addClassName("active");
            activeTab = tab;
        }

        container.add(component);
    }

    public void addExtraContent(Div... content) {
        extraContainer.add(content);
    }

    private void onTabSelect(ElementClickEvent event) {
        if (activeTab != null) {
            activeTab.removeClassName("active");

            String uuid = (String) activeTab.getUserData("UUID");
            Div activeComponent = (Div) components.get(uuid);
            activeComponent.setVisible(false);
        }

        Div tab = (Div) event.getSource();
        tab.addClassName("active");
        activeTab = tab;

        String uuid = (String) tab.getUserData("UUID");
        Div component = (Div) components.get(uuid);
        component.setVisible(true);
    }
    
}
