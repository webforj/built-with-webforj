package com.webforjrest.views;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.component.tabbedpane.TabbedPane.Alignment;
import com.webforj.component.tabbedpane.TabbedPane.Placement;
import com.webforj.router.annotation.Route;
import com.webforjrest.service.CustomerDelegatingRepository;
import com.webforjrest.service.RestClientService;

/**
 * Main layout that contains tabs for Customer Management views.
 */
@Route("/")
public class MainLayout extends Composite<FlexLayout> {

  private final RestClientService customerService;
  private final CustomerDelegatingRepository customerRepository;

  private FlexLayout container = getBoundComponent();
  private TabbedPane tabbedPane;

  public MainLayout(RestClientService customerService,
                    CustomerDelegatingRepository customerRepository) {
    this.customerService = customerService;
    this.customerRepository = customerRepository;

    initializeComponents();
    setupLayout();
  }

  private void initializeComponents() {
    tabbedPane = new TabbedPane();
    tabbedPane.setPlacement(Placement.TOP);
    tabbedPane.setAlignment(Alignment.START);

    // Set fixed dimensions for the tabbed pane to prevent resizing
    tabbedPane.setStyle("width", "100%");
    tabbedPane.setStyle("max-width", "1400px");
    tabbedPane.setStyle("height", "calc(100vh - 4rem)");

    // Create CustomerDisplay - loads all data into memory with CollectionRepository
    CustomerDisplay customerView = new CustomerDisplay(customerService);

    // Create CustomerDelegatingDisplay - uses DelegatingRepository for lazy loading
    CustomerDelegatingDisplay customerDelegatingView = new CustomerDelegatingDisplay(customerRepository);

    // Add tabs
    tabbedPane.addTab("CollectionRepository (All in Memory)", customerView);
    tabbedPane.addTab("DelegatingRepository (Lazy Loading)", customerDelegatingView);
  }

  private void setupLayout() {
    container.setDirection(FlexDirection.COLUMN);
    container.setAlignment(FlexAlignment.CENTER);
    container.setStyle("width", "100%");
    container.setStyle("height", "100vh");
    container.setStyle("overflow", "hidden");
    container.add(tabbedPane);
  }
}
