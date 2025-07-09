package com.webforj.builtwithwebforj.dashboard;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "com.webforj.builtwithwebforj.dashboard.views")
@StyleSheet("ws://app.css")
@StyleSheet("ws://dashboard-view.css")
@AppTheme("light")
@AppProfile(name = "webforj-dashboard", shortName = "webforj-dashboard")
public class Application extends App {
}
