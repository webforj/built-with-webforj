package com.webforj.locationtracker;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.AppTitle;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;

@Routify(packages = "com.webforj.locationtracker.views")
@BundleEntry("app.css")
@AppTheme("light")
@AppTitle("Friends")
@AppProfile(name = "Friends", shortName = "Friends")
public class Application extends App {
}
