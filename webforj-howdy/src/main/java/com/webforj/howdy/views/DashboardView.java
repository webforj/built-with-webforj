package com.webforj.howdy.views;

import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.environment.namespace.PrivateNamespace;
import com.webforj.environment.namespace.event.NamespaceChangeEvent;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.howdy.components.NoData;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.annotation.RouteAlias;
import com.webforj.component.googlecharts.GoogleChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The `DashboardView` class represents the main dashboard view in the application.
 * It visualizes user mood distributions by displaying a bar chart that dynamically
 * updates based on data from a designated namespace model.
 *
 * The view is constructed using a vertically-centered flex layout, which contains
 * a `GoogleChart` component for rendering the user mood data. This data is sourced
 * from the `PrivateNamespace` model, ensuring thread-safe interactions and accurate
 * synchronization.
 *
 * Key features of the `DashboardView` include:
 * - Centered layout with flexible alignment and styling.
 * - A dynamically updating bar chart component to show user mood counts.
 * - A data model interface via a private namespace for data storage and updates.
 * - Automated handling of namespace changes to reflect real-time updates in the chart.
 *
 * An instance of the `DashboardView` listens for changes in its model and updates
 * the displayed data in the chart dynamically. This ensures that the dashboard
 * reflects the latest metrics with minimal user interaction.
 *
 **/
@Route(value = "/", outlet = MainLayout.class)
@RouteAlias(value = "/dashboard")
@FrameTitle("Dashboard")
public class DashboardView extends Composite<FlexLayout> {

  private final ListenerRegistration<NamespaceChangeEvent> eventListenerReg;


  /**
   * Finalizes the destruction of the view and ensures cleanup of resources.
   *
   * This method is called during the teardown of the `DashboardView` component
   * to perform custom destruction logic before the view is fully destroyed.
   * It removes the registered event listener to prevent memory leaks or unintended
   * event processing after the view is no longer active.
   *
   * The base class's `onDidDestroy` method is invoked first to ensure any generic
   * destruction handled by the superclass is performed.
   */
  @Override
  protected void onDidDestroy() {
    super.onDidDestroy();
    this.eventListenerReg.remove();
  }

  /**
   * Represents the current instance of the layout bound to the view.
   *
   * This variable is an instance of `FlexLayout` and serves as the root layout
   * for the `UsersView` class. It is initialized by invoking the `getBoundComponent`
   * method, which links the layout component to the view. The `self` variable
   * is used to configure the overall layout of the view, including its alignment,
   * dimensions, and the addition of child components.
   */
  private FlexLayout self = getBoundComponent();

  /**
   * Represents the main chart component displayed in the `DashboardView`.
   *
   * The `chart` variable is an instance of `GoogleChart` and is initialized with the chart type
   * set to `BAR`. This chart serves as a visual representation of user mood distributions based
   * on the dynamically updated data managed by the associated namespace model.
   *
   * The `chart` is styled and embedded within a flex-based, vertically centered layout structure.
   * It dynamically updates its data and appearance whenever changes are made to the namespace model,
   * ensuring it reflects the latest user mood metrics.
   *
   * The primary responsibilities of this variable include:
   * - Displaying mood distribution data in a bar chart format.
   * - Dynamically rendering and updating based on model data.
   * - Serving as the central visual component of the dashboard view.
   */
  GoogleChart chart = new GoogleChart(GoogleChart.Type.BAR);

  NoData noData = new NoData();

  /**
   * Represents a `PrivateNamespace` instance used for managing and storing
   * application-specific data related to the "HowdyApp" in the "Board" namespace.
   *
   * This variable serves as a centralized data model for storing and retrieving
   * user-specific information (like user moods) within the application. It is
   * scoped to the "HowdyApp" application and ensures data isolation with locking
   * mechanisms to prevent concurrent modifications.
   *
   * Key responsibilities:
   * - Facilitates secure data storage for the "HowdyApp".
   * - Provides mechanisms to interact with and manage namespace-specific data.
   * - Ensures thread-safe operations in a multi-user environment through locking.
   *
   * The `true` parameter specifies that the namespace is private, ensuring that
   * data within this namespace is isolated from other namespaces in the application.
   */
  PrivateNamespace model = new PrivateNamespace("HowdyApp", "Board", true);

  /**
   * Represents the main dashboard view of the application.
   * The dashboard displays a chart summarizing user mood distributions
   * based on data collected and managed by the associated namespace model.
   *
   * The view is composed of a flex layout with center alignment, using a
   * column-based, vertically-centered layout structure. The primary component
   * of the dashboard is a pie chart, styled with 3D rendering and a width
   * of 80% of the available space.
   *
   * The chart dynamically updates whenever there are changes in the namespace
   * model, ensuring that the displayed data reflects the latest user mood
   * distribution.
   *
   * Functionalities of this view include:
   * - Initialization of the visual layout and chart appearance.
   * - Dynamic data binding between the chart and the namespace model.
   * - Immediate rendering of the current data upon initialization.
   */
  public DashboardView() {

    self.setHeight("100%");
    self.setAlignment(FlexAlignment.CENTER);
    self.setDirection(FlexDirection.COLUMN);
    self.setJustifyContent(FlexJustifyContent.CENTER);

    Map<String, Object> options = new HashMap<>();
    options.put("title", "The Mood of our Users.");
    options.put("is3D", "true");

    chart.setOptions(options);
    chart.setStyle("width","80%");
    chart.setStyle("display","none");

    self.add(noData,chart);

    this.eventListenerReg = model.onChange(this::updateData);
    updateData(null);

  }

  /**
   * Updates the chart data to display mood counts from the namespace model.
   * This method is triggered by changes in the namespace and processes
   * the data to update the chart view with the current mood distribution.
   *
   * @param namespaceChangeEvent the event that represents changes in the namespace model;
   *                              can be null if method is invoked manually without an event
   */
  private void updateData(NamespaceChangeEvent namespaceChangeEvent) {
    // Initialize chart data
    List<Object> data = new ArrayList<>();

    data.add(Arrays.asList("Mood", "Count")); // header

    // Count distinct moods
    Map<String, Integer> moodCounts = model.keySet()
      .stream()
      .map(model::get)
      .collect(Collectors.groupingBy(Object::toString,
        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
      ));

    // Add mood-count rows to chart data
    moodCounts.forEach((mood, count) ->
      data.add(Arrays.asList(mood, count))
    );
    chart.setData(data);

    noData.setVisible(moodCounts.isEmpty());
    chart.setStyle("display",moodCounts.isEmpty()?"none":"block");
  }
}
