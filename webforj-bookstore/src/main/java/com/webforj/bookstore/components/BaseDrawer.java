package com.webforj.bookstore.components;

import com.webforj.component.Composite;
import com.webforj.component.Component;
import com.webforj.component.drawer.Drawer;
import com.webforj.component.drawer.Drawer.Placement;

/**
 * A base class for drawers in the application, ensuring consistent behavior and
 * styling.
 * 
 * @author webforJ Bookstore
 */
@com.webforj.annotation.StyleSheet("ws://drawer.css")
public abstract class BaseDrawer extends Composite<Drawer> {

    private Drawer self = getBoundComponent();

    public BaseDrawer() {
        configureDrawer();
        self.close();
    }

    private void configureDrawer() {
        self.setPlacement(Placement.BOTTOM_CENTER);
        self.addClassName("bookstore-drawer");
    }

    /**
     * Opens the drawer.
     */
    public void open() {
        self.open();
    }

    /**
     * Closes the drawer.
     */
    public void close() {
        self.close();
    }

    /**
     * Sets the content of the drawer.
     * 
     * @param content the component to display inside the drawer
     */
    protected void setContent(Component content) {
        self.removeAll();
        self.add(content);
    }
}
