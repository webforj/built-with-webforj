package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.Element;
import com.webforj.component.element.ElementComposite;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.EventName;
import com.webforj.component.element.annotation.EventOptions;
import com.webforj.component.element.annotation.EventOptions.EventData;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.component.event.ComponentEvent;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasSize;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasVisibility;
import com.webforj.dispatcher.ListenerRegistration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The {@code ui5-table} element with its header, rows and cells.
 *
 * <p>
 * The table shows a list of Java objects. A column is a header text and a function that reads the
 * cell text from an object, which is how the webforJ table works too.
 * </p>
 *
 * @param <T> the type of the items shown, one per row
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Table.js")
@BundleEntry("@ui5/webcomponents/dist/TableHeaderRow.js")
@BundleEntry("@ui5/webcomponents/dist/TableHeaderCell.js")
@BundleEntry("@ui5/webcomponents/dist/TableRow.js")
@BundleEntry("@ui5/webcomponents/dist/TableCell.js")
@NodeName("ui5-table")
public class Ui5Table<T> extends ElementComposite implements HasVisibility<Ui5Table<T>>,
    HasSize<Ui5Table<T>>, HasClassName<Ui5Table<T>>, HasStyle<Ui5Table<T>> {
  private final PropertyDescriptor<String> noDataTextProp =
      PropertyDescriptor.property("noDataText", "");
  private final Element headerRow = new Element("ui5-table-header-row");
  private final List<Function<T, String>> columns = new ArrayList<>();
  private final List<Element> rows = new ArrayList<>();
  private List<T> items = List.of();

  /**
   * Creates an empty table.
   */
  public Ui5Table() {
    getElement().add("headerRow", headerRow);
  }

  /**
   * Adds a column.
   *
   * @param header the header text
   * @param text reads the cell text from an item
   * @return this table
   */
  public Ui5Table<T> addColumn(String header, Function<T, String> text) {
    columns.add(text);
    headerRow.add(cell("ui5-table-header-cell", header));
    return this;
  }

  /**
   * Shows the given items, one row each, in place of the current rows.
   *
   * @param items the items
   * @return this table
   */
  public Ui5Table<T> setItems(List<T> items) {
    this.items = List.copyOf(items);
    getElement().remove(rows.toArray(Element[]::new));
    rows.clear();
    for (int i = 0; i < this.items.size(); i++) {
      rows.add(row(i, this.items.get(i)));
    }
    getElement().add(rows.toArray(Element[]::new));

    return this;
  }

  /**
   * Gets the items shown.
   *
   * @return the items
   */
  public List<T> getItems() {
    return items;
  }

  /**
   * Sets the text shown when there are no items.
   *
   * @param noDataText the text
   * @return this table
   */
  public Ui5Table<T> setNoDataText(String noDataText) {
    set(noDataTextProp, noDataText);
    return this;
  }

  /**
   * Gets the text shown when there are no items.
   *
   * @return the text
   */
  public String getNoDataText() {
    return get(noDataTextProp);
  }

  /**
   * Adds a listener for a click on a row. It receives the item of that row.
   *
   * @param listener the listener
   * @return the registration
   */
  public ListenerRegistration<RowClickEvent> onItemClick(Consumer<T> listener) {
    return addEventListener(RowClickEvent.class, event -> {
      // The click can come from a row the browser still showed after the items were replaced.
      if (event.getIndex() < items.size()) {
        listener.accept(items.get(event.getIndex()));
      }
    });
  }

  // The row key is the position of the item, which is all the browser needs to report a click.
  private Element row(int index, T item) {
    Element row = new Element("ui5-table-row");
    row.setProperty("rowKey", String.valueOf(index));
    row.setProperty("interactive", true);
    for (Function<T, String> text : columns) {
      row.add(cell("ui5-table-cell", text.apply(item)));
    }

    return row;
  }

  private static Element cell(String node, String text) {
    Element cell = new Element(node);
    cell.setText(text);
    return cell;
  }

  /**
   * Fired when the user clicks a row. The browser sends only the key of that row along.
   */
  @EventName("row-click")
  @EventOptions(data = @EventData(key = "rowKey", exp = "event.detail.row.rowKey"))
  public static final class RowClickEvent extends ComponentEvent<Ui5Table<?>> {

    /**
     * Creates the event.
     *
     * @param source the table
     * @param data the event data
     */
    public RowClickEvent(Ui5Table<?> source, Map<String, Object> data) {
      super(source, data);
    }

    /**
     * Gets the position of the clicked row.
     *
     * @return the position
     */
    public int getIndex() {
      return Integer.parseInt(String.valueOf(getData().get("rowKey")));
    }
  }
}
