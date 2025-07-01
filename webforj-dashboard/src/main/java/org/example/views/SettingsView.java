package org.example.views;

import org.example.components.settings.SettingsSection;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.NumberField;
import com.webforj.component.field.TextField;
import com.webforj.component.field.PasswordField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.optioninput.RadioButton;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

@Route(value = "settings", outlet = MainLayout.class)
@StyleSheet("ws://settings-view.css")
@FrameTitle("Settings")
public class SettingsView extends Composite<FlexLayout> {
  private final FlexLayout self = getBoundComponent();
  private TabbedPane settingsTabs;

  public SettingsView() {
    self.addClassName("settings-view");
    self.setDirection(FlexDirection.COLUMN);

    createHeader();
    createSettingsTabs();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.addClassName("settings-view__header");
    header.setDirection(FlexDirection.COLUMN);

    H2 title = new H2("Settings");
    title.addClassName("settings-view__title");

    Paragraph description = new Paragraph("Manage your application preferences and configuration");
    description.addClassName("settings-view__description");

    header.add(title, description);
    self.add(header);
  }

  private void createSettingsTabs() {
    // Create container for tabs
    Div tabsContainer = new Div();
    tabsContainer.addClassName("settings-view__tabs-container");

    settingsTabs = new TabbedPane();
    settingsTabs.addClassName("settings-view__tabs");

    // Create content components for each tab
    FlexLayout generalContent = createGeneralSettings();
    FlexLayout notificationContent = createNotificationSettings();
    FlexLayout apiContent = createApiSettings();
    FlexLayout privacyContent = createPrivacySettings();

    // Add tabs with their associated content components
    settingsTabs.addTab("General", generalContent);
    settingsTabs.addTab("Notifications", notificationContent);
    settingsTabs.addTab("API Keys", apiContent);
    settingsTabs.addTab("Privacy", privacyContent);

    // Add tabs to the container
    tabsContainer.add(settingsTabs);
    self.add(tabsContainer);
  }

  private FlexLayout createGeneralSettings() {
    FlexLayout content = new FlexLayout();
    content.addClassName("settings-view__content");
    content.setDirection(FlexDirection.COLUMN);

    // Display preferences
    SettingsSection displaySection = createSettingsSection("Display Preferences",
        "Configure your display and currency preferences");

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
    displaySection.addContent(displayContainer);

    // Data refresh
    SettingsSection dataSection = createSettingsSection("Data Refresh", "Configure how often data is updated");

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
    dataSection.addContent(dataContainer);

    content.add(displaySection, dataSection);
    return content;
  }

  private FlexLayout createNotificationSettings() {
    FlexLayout content = new FlexLayout();
    content.addClassName("settings-view__content");
    content.setDirection(FlexDirection.COLUMN);

    // Email notifications
    SettingsSection emailSection = createSettingsSection("Email Notifications",
        "Manage your email notification preferences");

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
    emailSection.addContent(emailContainer);

    // Push notifications
    SettingsSection pushSection = createSettingsSection("Push Notifications",
        "Configure desktop and mobile push notifications");

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
    pushSection.addContent(pushContainer);

    // Alert thresholds
    SettingsSection thresholdSection = createSettingsSection("Alert Thresholds",
        "Set when to receive alerts based on price and volume changes");

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
    thresholdSection.addContent(thresholdContainer);

    content.add(emailSection, pushSection, thresholdSection);
    return content;
  }

  private FlexLayout createApiSettings() {
    FlexLayout content = new FlexLayout();
    content.addClassName("settings-view__content");
    content.setDirection(FlexDirection.COLUMN);

    // API configuration
    SettingsSection apiSection = createSettingsSection("API Configuration", "Configure your exchange API settings");

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
    apiSection.addContent(apiContainer);

    // Webhooks
    SettingsSection webhookSection = createSettingsSection("Webhooks",
        "Configure webhook endpoints for real-time updates");

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
    webhookSection.addContent(webhookContainer);

    content.add(apiSection, webhookSection);
    return content;
  }

  private FlexLayout createPrivacySettings() {
    FlexLayout content = new FlexLayout();
    content.addClassName("settings-view__content");
    content.setDirection(FlexDirection.COLUMN);

    // Data privacy
    SettingsSection privacySection = createSettingsSection("Data Privacy",
        "Control how your data is collected and used");

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
    privacySection.addContent(privacyContainer);

    // Data management
    SettingsSection dataManagementSection = createSettingsSection("Data Management",
        "Export or delete your personal data");

    Button exportData = new Button("Export My Data");
    exportData.setPrefixComponent(TablerIcon.create("download"));

    Button deleteData = new Button("Delete My Data");
    deleteData.setPrefixComponent(TablerIcon.create("trash"));
    deleteData.setTheme(ButtonTheme.DANGER);

    FlexLayout buttonGroup = new FlexLayout();
    buttonGroup.setSpacing("var(--dwc-space-m)")
        .setWrap(FlexWrap.WRAP);
    buttonGroup.add(exportData, deleteData);

    dataManagementSection.addContent(buttonGroup);

    // Account
    SettingsSection accountSection = createSettingsSection("Account", "Manage your account settings and security");

    Button changePassword = new Button("Change Password");
    changePassword.setPrefixComponent(TablerIcon.create("key"));

    Button deactivateAccount = new Button("Deactivate Account");
    deactivateAccount.setTheme(ButtonTheme.OUTLINED_DANGER);

    FlexLayout accountButtons = new FlexLayout();
    accountButtons.setSpacing("var(--dwc-space-m)")
        .setWrap(FlexWrap.WRAP);
    accountButtons.add(changePassword, deactivateAccount);

    accountSection.addContent(accountButtons);

    content.add(privacySection, dataManagementSection, accountSection);
    return content;
  }

  private SettingsSection createSettingsSection(String title, String description) {
    return new SettingsSection(title, description);
  }
}