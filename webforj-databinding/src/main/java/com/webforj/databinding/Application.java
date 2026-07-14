package com.webforj.databinding;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "com.webforj.databinding.views")
@StyleSheet("ws://app.css")
@AppTheme("system")
@AppProfile(name = "webforj-databinding", shortName = "webforj-databinding")
public class Application extends App {
}
