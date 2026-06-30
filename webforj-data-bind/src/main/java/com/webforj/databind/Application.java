package com.webforj.databind;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "com.webforj.databind.views")
@StyleSheet("ws://app.css")
@AppTheme("system")
@AppProfile(name = "webforj-databind", shortName = "webforj-databind")
public class Application extends App {
}
