package com.webforj.builtwithwebforj.focustracker.views;

import com.webforj.Interval;
import com.webforj.Page;
import com.webforj.component.Composite;
import com.webforj.component.desktopnotification.DesktopNotification;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

@Route("/")
@FrameTitle("Focus Tracker")
public class FocusTrackerView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private Div timerDisplay = new Div();
  private Button minusBtn = new Button();
  private Button plusBtn = new Button();
  private Button startBtn = new Button("Start");
  private Button completeBtn = new Button("Complete");
  private Paragraph status = new Paragraph("Ready to focus");

  private int minutes = 5;
  private int seconds = 0;
  private boolean isRunning = false;
  private Interval timerInterval;

  public FocusTrackerView() {
    self.setDirection(FlexDirection.COLUMN)
        .setAlignment(FlexAlignment.CENTER)
        .setJustifyContent(FlexJustifyContent.CENTER)
        .addClassName("focus-tracker");
    buildUI();
    updateBadge();
  }

  private void buildUI() {
    H1 title = new H1("Focus Tracker");
    title.addClassName("focus-tracker__title");

    Paragraph subtitle = new Paragraph("Stay focused, stay productive");
    subtitle.addClassName("focus-tracker__subtitle");

    FlexLayout timerRow = new FlexLayout();
    timerRow.setDirection(FlexDirection.ROW)
        .setAlignment(FlexAlignment.CENTER)
        .addClassName("focus-tracker__timer-row");

    minusBtn.setTheme(ButtonTheme.OUTLINED_PRIMARY);
    minusBtn.setIcon(TablerIcon.create("minus"));
    minusBtn.addClickListener(e -> adjustTime(-1));

    timerDisplay.setText(formatTime(minutes, seconds));
    timerDisplay.addClassName("focus-tracker__display");

    plusBtn.setTheme(ButtonTheme.OUTLINED_PRIMARY);
    plusBtn.setIcon(TablerIcon.create("plus"));
    plusBtn.addClickListener(e -> adjustTime(1));

    timerRow.add(minusBtn, timerDisplay, plusBtn);

    status.addClassName("focus-tracker__status");

    FlexLayout buttonRow = new FlexLayout();
    buttonRow.setDirection(FlexDirection.ROW)
        .addClassName("focus-tracker__buttons");

    startBtn.setTheme(ButtonTheme.PRIMARY);
    startBtn.setPrefixComponent(TablerIcon.create("player-play"));
    startBtn.addClickListener(e -> toggleTimer());

    completeBtn.setTheme(ButtonTheme.OUTLINED_PRIMARY);
    completeBtn.setPrefixComponent(TablerIcon.create("check"));
    completeBtn.addClickListener(e -> completeSession());

    buttonRow.add(startBtn, completeBtn);

    Paragraph hint = new Paragraph("Install this app to see the badge on your app icon");
    hint.addClassName("focus-tracker__hint");

    self.add(title, subtitle, timerRow, status, buttonRow, hint);
  }

  private void adjustTime(int delta) {
    if (isRunning)
      return;
    minutes = Math.max(1, Math.min(60, minutes + delta));
    seconds = 0;
    timerDisplay.setText(formatTime(minutes, seconds));
    updateBadge();
  }

  private void toggleTimer() {
    if (isRunning) {
      pauseTimer();
    } else {
      startTimer();
    }
  }

  private void startTimer() {
    isRunning = true;
    startBtn.setText("Pause");
    startBtn.setPrefixComponent(TablerIcon.create("player-pause"));
    status.setText("Focus mode active");
    minusBtn.setEnabled(false);
    plusBtn.setEnabled(false);
    updateBadge();

    timerInterval = new Interval(1f, e -> tick());
    timerInterval.start();
  }

  private void pauseTimer() {
    isRunning = false;
    startBtn.setText("Resume");
    startBtn.setPrefixComponent(TablerIcon.create("player-play"));
    status.setText("Paused");
    minusBtn.setEnabled(true);
    plusBtn.setEnabled(true);

    if (timerInterval != null) {
      timerInterval.stop();
    }
  }

  private void tick() {
    if (seconds > 0) {
      seconds--;
    } else if (minutes > 0) {
      minutes--;
      seconds = 59;
    }

    timerDisplay.setText(formatTime(minutes, seconds));
    updateBadge();

    if (minutes == 0 && seconds == 0) {
      completeSession();
    }
  }

  private void completeSession() {
    isRunning = false;

    if (timerInterval != null) {
      timerInterval.stop();
      timerInterval = null;
    }

    status.setText("Session complete!");
    startBtn.setText("Start");
    startBtn.setPrefixComponent(TablerIcon.create("player-play"));
    minusBtn.setEnabled(true);
    plusBtn.setEnabled(true);

    clearBadge();
    DesktopNotification.show("Focus Session Complete!", "Great work! Take a break.");
  }

  private void updateBadge() {
    int value = minutes > 0 ? minutes : (seconds > 0 ? 1 : 0);
    Page.getCurrent().executeJsVoidAsync("navigator.setAppBadge(" + value + ")");
  }

  private void clearBadge() {
    Page.getCurrent().executeJsVoidAsync("navigator.clearAppBadge()");
  }

  private String formatTime(int mins, int secs) {
    return String.format("%02d:%02d", mins, secs);
  }

  @Override
  protected void onDestroy() {
    if (timerInterval != null) {
      timerInterval.stop();
    }
  }
}
