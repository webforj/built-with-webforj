package com.webforj.howdy.views;

import com.webforj.Page;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.event.ButtonClickEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.list.ListBox;
import com.webforj.component.toast.Toast;
import com.webforj.environment.namespace.PrivateNamespace;
import com.webforj.environment.namespace.exception.NamespaceLockedException;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.howdy.util.NicknameGenerationException;
import com.webforj.howdy.util.NicknameGenerator;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.List;

@Route(value = "/you", outlet = MainLayout.class)
@FrameTitle("You")
public class YouView extends Composite<FlexLayout> {

  /**
   * Represents the current instance of the layout bound to the view.
   *
   * This variable is an instance of `FlexLayout` and serves as the root layout
   * for the `UsersView` class. It is initialized by invoking the `getBoundComponent`
   * method, which links the layout component to the view. The `self` variable
   * is used to configure the overall layout of the view, including its alignment,
   * dimensions, and the addition of child components.
   */
  private FlexLayout self = getBoundComponent();

  /**
   * Represents a `PrivateNamespace` instance used for managing and storing
   * application-specific data related to the "HowdyApp" in the "Board" namespace.
   *
   * This variable serves as a centralized data model for storing and retrieving
   * user-specific information (like user moods) within the application. It is
   * scoped to the "HowdyApp" application and ensures data isolation with locking
   * mechanisms to prevent concurrent modifications.
   *
   * Key responsibilities:
   * - Facilitates secure data storage for the "HowdyApp".
   * - Provides mechanisms to interact with and manage namespace-specific data.
   * - Ensures thread-safe operations in a multi-user environment through locking.
   *
   * The `true` parameter specifies that the namespace is private, ensuring that
   * data within this namespace is isolated from other namespaces in the application.
   */
  PrivateNamespace model = new PrivateNamespace("HowdyApp", "Board", true);

  /**
   * Represents a user input field for entering a nickname.
   *
   * The `Nickname` field is a `TextField` component initialized with a
   * placeholder label "Your Nickname:". This field is used to allow users
   * to input their nickname, which may be validated or processed further
   * within the application.
   *
   * Key characteristics:
   * - Serves as an interface for user nickname input.
   * - Includes a prompt text label to guide the user.
   */
  TextField nicknameInput = new TextField("Your Nickname:");

  /**
   * Represents a list for selecting or displaying the user's mood.
   *
   * The `MyMood` variable is an instance of the `ListBox` component,
   * initialized with the label "My Mood:". This component allows users to interact
   * with and select their current mood from a predefined list of options.
   *
   * The `MyMood` field plays a critical role in the user interface, enabling users to
   * express their current emotional state effectively. It can be used in combination
   * with other form elements to collect and process user input.
   */
  ListBox myMoodSelection = new ListBox("My Mood:");

  /**
   * Represents a button labeled "Submit" in the context of the `YouView` class.
   *
   * This button is intended to trigger an action within the view, such as submitting
   * a form or invoking specific business logic. The `Submit` button's click event
   * can be handled by a dedicated method to implement the desired functionality,
   * such as validating input or updating data.
   *
   * Key characteristics:
   * - Contains the label "Submit" for user interaction.
   * - Can be integrated with event-handling mechanisms for click actions.
   */
  Button submitButton = new Button("Submit");

  /**
   * Represents the nickname of a user retrieved from the current page's attributes.
   *
   * This variable holds the value of the "nickname" attribute associated with the
   * current page. It is used to uniquely identify or personalize user interactions within
   * the view. The value of `nickname` is dynamically obtained from the `Page` instance.
   *
   * Key Characteristics:
   * - Dynamically retrieves the attribute named "nickname" from the current page.
   * - Used within the context of the view to reference or validate user-specific identifiers.
   */
  String currentUserNickname = Page.getCurrent().getAttribute("nickname");

  /**
   * A predefined list of mood options available for selection in the "MyMood" dropdown component.
   *
   * This collection contains a variety of moods, each represented as a string with an associated emoji
   * to visually convey the mood. These mood options are used to populate the "MyMood" selector in the
   * user interface, allowing users to express their current emotional state.
   *
   * The list includes moods such as "Happy 😊", "Joyful 😄", and "Excited 🎉", among others.
   * The order of the moods is preserved as defined in this list.
   *
   * This list is immutable and cannot be modified after initialization. It provides a consistent
   * set of predefined moods for use throughout the application.
   */
  private static final List<String> AVAILABLE_MOODS = List.of(
    "😊 Happy",
    "👍 Enthusiastic",
    "🙏 Grateful",
    "💡 Inspired",
    "💪 Confident",
    "😌 Relaxed",
    "😄 Joyful",
    "🏆 Proud",
    "🌈 Optimistic",
    "😜 Playful",
    "❤️ Loved",
    "🎉 Excited"
  );

  /**
   * Represents the font size styling applied to the "MyMood" dropdown component.
   *
   * This constant defines the font size used to enhance the visual appearance of the
   * dropdown menu in the YouView class. It is applied during the initialization
   * of the "MyMood" selector to improve readability and provide a better user experience.
   *
   * Value: "larger"
   */
  private static final String MOOD_FONT_SIZE = "larger";

  /**
   * Constructs a new instance of the YouView class, initializing the user interface components
   * and setting up the view's layout and functionality.
   *
   * This constructor configures the layout of the view with a vertical column alignment and
   * full height. It initializes the "MyMood" dropdown with a selection of predefined mood options
   * and sets the default selected index to the first item. Additionally, the display style of
   * "MyMood" is adjusted by increasing its font size.
   *
   * The constructor manages the behavior of the "Nickname" input field, generating a unique nickname
   * if none is provided or pre-populating it if one exists. It also sets focus on the nickname
   * input if it is empty.
   *
   * The "Submit" button's click event is registered to trigger the onSubmit method, allowing users
   * to share their selected mood and nickname. The constructor ensures that the UI is properly
   * initialized for interaction and use.
   */
  public YouView() {
    self.setMaxWidth("320px");
    self.setStyle("margin", "var(--dwc-space-m) auto");
    self.setDirection(FlexDirection.COLUMN);

    initializeMoodSelector();

    self.add(nicknameInput, myMoodSelection, submitButton);

    submitButton.onClick(this::onSubmit);

    if (this.currentUserNickname.isBlank()) {
      try {
        nicknameInput.setText(NicknameGenerator.generateUniqueNickname());
      } catch (NicknameGenerationException e) {
        //can't generate Nickname proposal, so just let the user enter one.
        nicknameInput.setText("");
      }
      nicknameInput.focus();
    }
    else {
      nicknameInput.setText(this.currentUserNickname);
      nicknameInput.setEnabled(false);
      String storedMood = (String) model.get(this.currentUserNickname);
      if (storedMood != null && !storedMood.isEmpty()) {
        for (com.webforj.component.list.ListItem item : myMoodSelection.getItems()) {
          if (item.getText().equals(storedMood)) {
            myMoodSelection.select(item);
            break;
          }
        }
      }
    }
  }

  /**
   * Initializes the "MyMood" selector component with predefined moods and styles.
   *
   * This method populates the "MyMood" dropdown with a collection of available moods
   * defined in the `AVAILABLE_MOODS` field. It sets the default selection to the first
   * mood in the list and applies a font size styling to the component using the value
   * specified in the `MOOD_FONT_SIZE` field.
   *
   * Responsibilities:
   * - Adds each mood from the `AVAILABLE_MOODS` collection to the "MyMood" dropdown.
   * - Selects the first mood (index 0) as the default.
   * - Styles the dropdown with a font size defined in `MOOD_FONT_SIZE`.
   */
  private void initializeMoodSelector() {
    AVAILABLE_MOODS.forEach(myMoodSelection::add);
    myMoodSelection.selectIndex(0);
    myMoodSelection.setStyle("font-size", MOOD_FONT_SIZE);
  }

  /**
   * Handles the submit button click event, allowing the user to share their selected mood
   * along with their nickname if the nickname is valid. The nickname is validated
   * and saved as an attribute, and the selected mood is stored in the model. If the operation
   * is successful, the nickname input is disabled, and a success message is displayed.
   *
   * @param buttonClickEvent the event triggered by the submit button click
   */
  private void onSubmit(ButtonClickEvent buttonClickEvent) {

      if (!this.currentUserNickname.isBlank() || Boolean.TRUE.equals(validateNickname())){

        Page.getCurrent().setAttribute("nickname", this.currentUserNickname);

        try {
          model.put(this.currentUserNickname, myMoodSelection.getSelectedItem().getText());
        } catch (NamespaceLockedException e) {
          throw new RuntimeException(e);
        }

        nicknameInput.setEnabled(false);

        Toast.show("You shared your mood, "+ currentUserNickname,1200,Theme.SUCCESS, Toast.Placement.TOP_RIGHT);

      }
  }

  /**
   * Validates the user-provided nickname and checks whether it meets the necessary criteria for submission.
   *
   * The validation ensures that the nickname is not empty and does not already exist in the model.
   * If the nickname is invalid, an error message is displayed, the input field is marked as invalid,
   * and focus is set on the nickname input field. If the nickname is valid, it is assigned to the
   * corresponding class attribute for further usage.
   *
   * @return true if the nickname passes all validation checks; false otherwise
   */
  private Boolean validateNickname() {
    String nick = nicknameInput.getText();
    if (nick.isBlank()) {
      nicknameInput.setInvalidMessage("Nickname cannot be empty");
      nicknameInput.setInvalid(true);
      nicknameInput.focus();
      return false;
    }

    if (nicknameInput.isEnabled() && model.contains(nick)){
          nicknameInput.setInvalidMessage("Nickname already exists");
          nicknameInput.setInvalid(true);
          nicknameInput.focus();
          return false;
        }


    this.currentUserNickname = nick;
    return true;
  }
}
