package com.webforjrest.views;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.component.tabbedpane.TabbedPane.Alignment;
import com.webforj.component.tabbedpane.TabbedPane.Placement;
import com.webforj.router.annotation.Route;
import com.webforjrest.service.JSONPlaceholderService;
import com.webforjrest.service.PostDelegatingRepository;
import com.webforjrest.service.RestClientService;

/**
 * Main layout that contains tabs for Customer Management and Posts views.
 */
@Route("/")
public class MainLayout extends Composite<FlexLayout> {

  private final RestClientService customerService;
  private final PostDelegatingRepository postRepository;

  private FlexLayout container = getBoundComponent();
  private TabbedPane tabbedPane;

  public MainLayout(RestClientService customerService,
                    PostDelegatingRepository postRepository,
                    JSONPlaceholderService jsonPlaceholderService) {
    this.customerService = customerService;
    this.postRepository = postRepository;

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

    // Create CustomerView without routing
    CustomerView customerView = new CustomerView(customerService);

    // Create PostsView without routing
    PostsView postsView = new PostsView(postRepository);

    // Add tabs
    tabbedPane.addTab("Customer Management", customerView);
    tabbedPane.addTab("JSONPlaceholder Posts", postsView);
  }

  private void setupLayout() {
    container.setDirection(FlexDirection.COLUMN);
    container.setAlignment(FlexAlignment.CENTER);
    container.setStyle("padding", "2rem");
    container.setStyle("width", "100%");
    container.setStyle("height", "100vh");
    container.setStyle("overflow", "hidden");
    container.add(tabbedPane);
  }
}
