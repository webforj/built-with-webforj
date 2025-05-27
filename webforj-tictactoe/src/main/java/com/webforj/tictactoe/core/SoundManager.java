package com.webforj.tictactoe.core;

import com.webforj.Page;
import com.webforj.environment.ObjectTable;

public final class SoundManager {

  private SoundManager() {
  }

  public static SoundManager getCurrent() {
    String name = SoundManager.class.getName();
    if (ObjectTable.contains(name)) {
      return (SoundManager) ObjectTable.get(name);
    }

    SoundManager instance = new SoundManager();
    ObjectTable.put(name, instance);

    return instance;
  }

  public void playClick() {
    play("click");
  }

  public void playWin() {
    play("win");
  }

  public void playLose() {
    play("lose");
  }

  public void playDraw() {
    play("draw");
  }

  public void playTap() {
    play("tap");
  }

  private void play(String name) {
    Page.getCurrent().executeJsVoidAsync(String.format("audioElements['%s'].play()", name));
  }
}
