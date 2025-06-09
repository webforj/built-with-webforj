package org.example.views;

import com.webforj.App;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.NumberField;
import com.webforj.component.field.TextField;
import com.webforj.component.field.PasswordField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optioninput.RadioButton;
import com.webforj.component.optioninput.RadioButtonGroup;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.tabbedpane.Tab;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

@Route(value = "settings", outlet = MainLayout.class)
@FrameTitle("Settings")
public class SettingsView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private TabbedPane settingsTabs;
  private boolean isDarkTheme;

  public SettingsView() {
    self.addClassName("settings-view");
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("overflow-y", "auto");
    self.setStyle("padding", "var(--dwc-space-l)");
    
    isDarkTheme = "dark".equals(App.getTheme());
    
    // Create header
    createHeader();
    
    // Create settings tabs
    createSettingsTabs();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.setDirection(FlexDirection.COLUMN)
          .setStyle("margin-bottom", "var(--dwc-space-xl)");
    
    H2 title = new H2("Settings");
    title.setStyle("margin", "0");
    
    Paragraph description = new Paragraph("Manage your application preferences and configuration");
    description.setStyle("color", "var(--dwc-color-default-color)");
    description.setStyle("margin", "var(--dwc-space-s) 0 0 0");
    
    header.add(title, description);
    self.add(header);
  }

  private void createSettingsTabs() {
    settingsTabs = new TabbedPane();
    settingsTabs.setStyle("margin-bottom", "var(--dwc-space-l)");
    
    settingsTabs.addTab(new Tab("General"));
    settingsTabs.addTab(new Tab("Notifications"));
    settingsTabs.addTab(new Tab("API Keys"));
    settingsTabs.addTab(new Tab("Privacy"));
    
    Div tabContent = new Div();
    tabContent.addClassName("settings-tab-content");
    
    // Show general settings by default
    showGeneralSettings(tabContent);
    
    settingsTabs.onSelect(e -> {
      tabContent.removeAll();
      Tab selectedTab = e.getTab();
      String tabText = selectedTab.getText();
      
      switch (tabText) {
        case "General":
          showGeneralSettings(tabContent);
          break;
        case "Notifications":
          showNotificationSettings(tabContent);
          break;
        case "API Keys":
          showApiSettings(tabContent);
          break;
        case "Privacy":
          showPrivacySettings(tabContent);
          break;
      }
    });
    
    self.add(settingsTabs, tabContent);
  }

  private void showGeneralSettings(Div container) {
    FlexLayout content = new FlexLayout();
    content.setDirection(FlexDirection.COLUMN)
           .setStyle("gap", "var(--dwc-space-xl)");
    
    
    // Display preferences
    Div displaySection = createSettingsSection("Display Preferences");
    
    FlexLayout displayContainer = new FlexLayout();
    displayContainer.setDirection(FlexDirection.COLUMN)
                    .setSpacing("var(--dwc-space-m)");
    
    ChoiceBox currencyBox = new ChoiceBox("Default Currency");
    currencyBox.add("USD");
    currencyBox.add("EUR");
    currencyBox.add("GBP");
    currencyBox.add("JPY");
    currencyBox.add("AUD");
    currencyBox.setValue("USD");
    
    ChoiceBox timeFormatBox = new ChoiceBox("Time Format");
    timeFormatBox.add("12-hour");
    timeFormatBox.add("24-hour");
    timeFormatBox.setValue("12-hour");
    
    displayContainer.add(currencyBox, timeFormatBox);
    displaySection.add(displayContainer);
    
    // Data refresh
    Div dataSection = createSettingsSection("Data Refresh");
    
    FlexLayout dataContainer = new FlexLayout();
    dataContainer.setDirection(FlexDirection.COLUMN)
                 .setSpacing("var(--dwc-space-m)");
    
    NumberField refreshInterval = new NumberField();
    refreshInterval.setLabel("Auto-refresh interval (seconds)");
    refreshInterval.setValue(30.0);
    refreshInterval.setMin(5.0);
    refreshInterval.setMax(300.0);
    refreshInterval.setStep(5.0);
    
    RadioButton autoRefreshSwitch = new RadioButton("Enable auto-refresh");
    autoRefreshSwitch.setSwitch(true);
    autoRefreshSwitch.setChecked(true);
    
    dataContainer.add(refreshInterval, autoRefreshSwitch);
    dataSection.add(dataContainer);
    
    content.add(displaySection, dataSection);
    container.add(content);
  }

  private void showNotificationSettings(Div container) {
    FlexLayout content = new FlexLayout();
    content.setDirection(FlexDirection.COLUMN)
           .setStyle("gap", "var(--dwc-space-xl)");
    
    // Email notifications
    Div emailSection = createSettingsSection("Email Notifications");
    
    FlexLayout emailContainer = new FlexLayout();
    emailContainer.setDirection(FlexDirection.COLUMN)
                  .setSpacing("var(--dwc-space-m)");
    
    RadioButton priceAlerts = new RadioButton("Price alerts");
    priceAlerts.setSwitch(true);
    priceAlerts.setChecked(true);
    
    RadioButton newsUpdates = new RadioButton("News updates");
    newsUpdates.setSwitch(true);
    newsUpdates.setChecked(false);
    
    RadioButton portfolioSummary = new RadioButton("Daily portfolio summary");
    portfolioSummary.setSwitch(true);
    portfolioSummary.setChecked(true);
    
    emailContainer.add(priceAlerts, newsUpdates, portfolioSummary);
    emailSection.add(emailContainer);
    
    // Push notifications
    Div pushSection = createSettingsSection("Push Notifications");
    
    FlexLayout pushContainer = new FlexLayout();
    pushContainer.setDirection(FlexDirection.COLUMN)
                 .setSpacing("var(--dwc-space-m)");
    
    RadioButton desktopNotifications = new RadioButton("Desktop notifications");
    desktopNotifications.setSwitch(true);
    desktopNotifications.setChecked(true);
    
    RadioButton soundAlerts = new RadioButton("Sound alerts");
    soundAlerts.setSwitch(true);
    soundAlerts.setChecked(false);
    
    pushContainer.add(desktopNotifications, soundAlerts);
    pushSection.add(pushContainer);
    
    // Alert thresholds
    Div thresholdSection = createSettingsSection("Alert Thresholds");
    
    FlexLayout thresholdContainer = new FlexLayout();
    thresholdContainer.setDirection(FlexDirection.COLUMN)
                      .setSpacing("var(--dwc-space-m)");
    
    NumberField priceChangeThreshold = new NumberField();
    priceChangeThreshold.setLabel("Price change threshold (%)");
    priceChangeThreshold.setValue(5.0);
    priceChangeThreshold.setMin(0.1);
    priceChangeThreshold.setMax(50.0);
    priceChangeThreshold.setStep(0.1);
    
    NumberField volumeChangeThreshold = new NumberField();
    volumeChangeThreshold.setLabel("Volume change threshold (%)");
    volumeChangeThreshold.setValue(20.0);
    volumeChangeThreshold.setMin(1.0);
    volumeChangeThreshold.setMax(100.0);
    volumeChangeThreshold.setStep(1.0);
    
    thresholdContainer.add(priceChangeThreshold, volumeChangeThreshold);
    thresholdSection.add(thresholdContainer);
    
    content.add(emailSection, pushSection, thresholdSection);
    container.add(content);
  }

  private void showApiSettings(Div container) {
    FlexLayout content = new FlexLayout();
    content.setDirection(FlexDirection.COLUMN)
           .setStyle("gap", "var(--dwc-space-xl)");
    
    // API configuration
    Div apiSection = createSettingsSection("API Configuration");
    
    FlexLayout apiContainer = new FlexLayout();
    apiContainer.setDirection(FlexDirection.COLUMN)
                .setSpacing("var(--dwc-space-m)");
    
    PasswordField apiKey = new PasswordField();
    apiKey.setLabel("API Key");
    apiKey.setPlaceholder("Enter your API key");
    
    PasswordField apiSecret = new PasswordField();
    apiSecret.setLabel("API Secret");
    apiSecret.setPlaceholder("Enter your API secret");
    
    ChoiceBox exchangeBox = new ChoiceBox("Default Exchange");
    exchangeBox.add("Binance");
    exchangeBox.add("Coinbase");
    exchangeBox.add("Kraken");
    exchangeBox.add("Bitfinex");
    
    apiContainer.add(apiKey, apiSecret, exchangeBox);
    apiSection.add(apiContainer);
    
    // Webhooks
    Div webhookSection = createSettingsSection("Webhooks");
    
    FlexLayout webhookContainer = new FlexLayout();
    webhookContainer.setDirection(FlexDirection.COLUMN)
                    .setSpacing("var(--dwc-space-m)");
    
    TextField webhookUrl = new TextField();
    webhookUrl.setLabel("Webhook URL");
    webhookUrl.setPlaceholder("https://your-webhook-url.com");
    
    RadioButton webhookEnabled = new RadioButton("Enable webhooks");
    webhookEnabled.setSwitch(true);
    webhookEnabled.setChecked(false);
    
    webhookContainer.add(webhookUrl, webhookEnabled);
    webhookSection.add(webhookContainer);
    
    content.add(apiSection, webhookSection);
    container.add(content);
  }

  private void showPrivacySettings(Div container) {
    FlexLayout content = new FlexLayout();
    content.setDirection(FlexDirection.COLUMN)
           .setStyle("gap", "var(--dwc-space-xl)");
    
    // Data privacy
    Div privacySection = createSettingsSection("Data Privacy");
    
    FlexLayout privacyContainer = new FlexLayout();
    privacyContainer.setDirection(FlexDirection.COLUMN)
                    .setSpacing("var(--dwc-space-m)");
    
    RadioButton analyticsTracking = new RadioButton("Allow analytics tracking");
    analyticsTracking.setSwitch(true);
    analyticsTracking.setChecked(true);
    
    RadioButton shareData = new RadioButton("Share usage data for improvements");
    shareData.setSwitch(true);
    shareData.setChecked(false);
    
    privacyContainer.add(analyticsTracking, shareData);
    privacySection.add(privacyContainer);
    
    // Data management
    Div dataManagementSection = createSettingsSection("Data Management");
    
    Button exportData = new Button("Export My Data");
    exportData.setPrefixComponent(TablerIcon.create("download"));
    
    Button deleteData = new Button("Delete All Data");
    deleteData.setPrefixComponent(TablerIcon.create("trash"));
    deleteData.setTheme(ButtonTheme.DANGER);
    
    FlexLayout buttonGroup = new FlexLayout();
    buttonGroup.setSpacing("var(--dwc-space-m)");
    buttonGroup.add(exportData, deleteData);
    
    dataManagementSection.add(buttonGroup);
    
    // Account
    Div accountSection = createSettingsSection("Account");
    
    Button changePassword = new Button("Change Password");
    changePassword.setPrefixComponent(TablerIcon.create("key"));
    
    Button deactivateAccount = new Button("Deactivate Account");
    deactivateAccount.setTheme(ButtonTheme.OUTLINED_DANGER);
    
    FlexLayout accountButtons = new FlexLayout();
    accountButtons.setSpacing("var(--dwc-space-m)");
    accountButtons.add(changePassword, deactivateAccount);
    
    accountSection.add(accountButtons);
    
    content.add(privacySection, dataManagementSection, accountSection);
    container.add(content);
  }

  private Div createSettingsSection(String title) {
    Div section = new Div();
    section.addClassName("settings-section");
    section.setStyle("background", "var(--dwc-surface-1)")
           .setStyle("border-radius", "var(--dwc-border-radius-m)")
           .setStyle("padding", "var(--dwc-space-l)");
    
    H3 sectionTitle = new H3(title);
    sectionTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0");
    
    section.add(sectionTitle);
    return section;
  }
}