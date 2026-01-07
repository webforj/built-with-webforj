package com.webforj.property;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.AppTitle;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

/**
 * PropertyView - Real Estate Property Listings Demo.
 *
 * <p>Showcases the webforJ Map Component through a familiar real estate browsing experience with
 * synchronized map and list views.
 */
@AppTitle("PropertyView")
@AppProfile(name = "PropertyView", shortName = "PropertyView")
@AppTheme("light")
@Routify(packages = "com.webforj.property.views")
@StyleSheet("ws://app.css")
public class Application extends App {}
