package com.pingpal;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(
  packages = {"com.pingpal.views"},
  defaultFrameName = "PingPal",
  initializeFrame = true,
  manageFramesVisibility = true,
  debug = true
)
@StyleSheet("ws://app.css")
@AppProfile(name = "PingPal", shortName = "PingPal")
public class Application extends App {
}
