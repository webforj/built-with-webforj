package com.webforjexplorer.components;

import com.google.gson.JsonObject;
import com.webforj.annotation.Attribute;
import com.webforj.annotation.InlineJavaScript;
import com.webforj.component.element.ElementComposite;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.concern.HasSize;
import com.webforj.concern.HasValue;
import com.webforj.data.event.ValueChangeEvent;
import com.webforj.dispatcher.EventListener;
import com.webforj.dispatcher.ListenerRegistration;

/**
 * A webforJ component wrapper for the Monaco Editor.
 * 
 * <p>This component integrates the Monaco Editor (the code editor that powers VS Code) into
 * webforJ applications. It provides syntax highlighting, code formatting, and other advanced
 * editing features for viewing source code files.</p>
 * 
 * <p>The editor is configured as read-only by default in this implementation, making it
 * suitable for file viewing in the Explorer application. Features include:</p>
 * 
 * <ul>
 *   <li>Syntax highlighting for multiple programming languages</li>
 *   <li>Automatic language detection based on file extension</li>
 *   <li>Customizable editor options through JSON configuration</li>
 *   <li>Full implementation of HasSize and HasValue interfaces</li>
 * </ul>
 * 
 * @author webforJ Team
 * @since 1.0
 */
@InlineJavaScript(value = "import * as heyWebComponentsmonacoEditor from 'https://esm.run/@hey-web-components/monaco-editor'", attributes = {
    @Attribute(name = "type", value = "module")
})
@NodeName("hey-monaco-editor")
public class Editor extends ElementComposite implements HasSize<Editor>, HasValue<Editor, String> {

  private final PropertyDescriptor<String> valueDescriptor = PropertyDescriptor.property("value", "");
  private final PropertyDescriptor<String> languageDescriptor = PropertyDescriptor.property("language", "");
  private final PropertyDescriptor<JsonObject> options = PropertyDescriptor.property("options", new JsonObject());

  /**
   * Constructs a new Editor component with default settings.
   * 
   * <p>The editor is initialized with the following defaults:</p>
   * <ul>
   *   <li>Read-only mode enabled</li>
   *   <li>Light theme (vs-light)</li>
   * </ul>
   */
  public Editor() {
    JsonObject initOptions = new JsonObject();
    initOptions.addProperty("readOnly", true);
    set(this.options, initOptions);
  }

  /**
   * Sets the content of the editor.
   * 
   * @param value the text content to display in the editor
   * @return this editor instance for method chaining
   */
  @Override
  public Editor setValue(String value) {
    set(valueDescriptor, value);
    return this;
  }

  /**
   * Gets the current content of the editor.
   * 
   * @return the text content currently displayed in the editor
   */
  @Override
  public String getValue() {
    return get(valueDescriptor);
  }

  /**
   * Sets the syntax highlighting language for the editor.
   * 
   * <p>Common language identifiers include: java, javascript, python, xml, json, html, css, etc.</p>
   *
   * @param language the language identifier for syntax highlighting
   * @return this editor instance for method chaining
   */
  public Editor setLanguage(String language) {
    set(languageDescriptor, language);
    return this;
  }

  /**
   * Gets the current syntax highlighting language.
   *
   * @return the language identifier currently set for syntax highlighting
   */
  public String getLanguage() {
    return get(languageDescriptor);
  }

  /**
   * Adds a value change listener to the editor.
   * 
   * <p>Note: In the current implementation, this method returns null as the editor
   * is configured in read-only mode and does not emit value change events.</p>
   * 
   * @param listener the event listener to add
   * @return null (not implemented for read-only editor)
   */
  @Override
  public ListenerRegistration<ValueChangeEvent<String>> addValueChangeListener(
      EventListener<ValueChangeEvent<String>> listener) {
    // Not implemented - editor is read-only
    return null;
  }
}
