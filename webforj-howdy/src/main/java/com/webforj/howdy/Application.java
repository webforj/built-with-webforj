package com.webforj.howdy;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "com.webforj.howdy.views")
@StyleSheet("ws://app.css")
@AppProfile(name = "Howdy", shortName = "Howdy")
public class Application extends App {}
