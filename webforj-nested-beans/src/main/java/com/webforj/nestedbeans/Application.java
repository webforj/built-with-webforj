package com.webforj.nestedbeans;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "com.webforj.nestedbeans.views")
@StyleSheet("ws://app.css")
@AppTheme("system")
@AppProfile(name = "webforj-nestedbeans", shortName = "webforj-nestedbeans")
public class Application extends App {
}
