package com.webforjexplorer;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

/**
 * Main application class for the webforJ Explorer - a file navigation showcase application.
 * 
 * <p>This application demonstrates the file navigation capabilities of the webforJ framework by
 * implementing a complete file explorer interface. Key features include:</p>
 * 
 * <ul>
 *   <li>Interactive file system navigation using the webforJ Tree component</li>
 *   <li>Integration with Monaco Editor for syntax-highlighted file viewing</li>
 *   <li>Dynamic file icon mapping based on file extensions</li>
 *   <li>Lazy-loading directory contents for optimal performance</li>
 *   <li>File metadata display with tooltips showing path, size, and modification date</li>
 *   <li>Responsive layout using webforJ's AppLayout component</li>
 * </ul>
 * 
 * <p>The application showcases how webforJ can be used to create desktop-like applications
 * in the browser, demonstrating component integration, event handling, and modern UI patterns.</p>
 * 
 * @author webforJ Team
 * @since 1.0
 */
@Routify(packages = "com.webforjexplorer.views")
@StyleSheet("ws://app.css")
@AppProfile(name = "webforJ Explorer", shortName = "webforJ Explorer")
public class Application extends App {
}
