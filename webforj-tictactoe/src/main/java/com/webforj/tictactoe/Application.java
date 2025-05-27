package com.webforj.tictactoe;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.JavaScript;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "com.webforj.tictactoe.views")
@StyleSheet("ws://app.css")
@JavaScript("ws://sound-manager.js")
@AppTheme("dark")
@AppProfile(name = "TicTacToe", shortName = "TTT", description = "A simple TicTacToe game built with webforJ")
public class Application extends App {
}
