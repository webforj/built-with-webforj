package org.example;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "org.example.views")
@StyleSheet("ws://app.css")
@AppTheme("light")
@AppProfile(name = "webforj-dashboard", shortName = "webforj-dashboard")
public class Application extends App {
}
