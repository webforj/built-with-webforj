package com.webforjexplorer.views;

import static com.webforj.component.tree.Tree.node;

import java.io.File;
import java.io.IOException;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.optiondialog.FileChooserDialog;
import com.webforj.component.tree.Tree;
import com.webforj.component.tree.TreeNode;
import com.webforj.component.tree.event.TreeExpandEvent;
import com.webforj.component.tree.event.TreeSelectEvent;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;
import com.webforjexplorer.components.Editor;
import com.webforjexplorer.utils.Utility;

/**
 * Main view of the webforJ Explorer application.
 * 
 * <p>This view implements a file explorer interface with:</p>
 * <ul>
 *   <li>A collapsible drawer containing the file tree navigation</li>
 *   <li>A main content area with Monaco Editor for viewing files</li>
 *   <li>Lazy-loading of directory contents for performance</li>
 *   <li>File selection and syntax-highlighted viewing</li>
 * </ul>
 * 
 * <p>The view uses webforJ's AppLayout for responsive design and implements
 * DidEnterObserver to prompt for directory selection on startup.</p>
 * 
 * @author webforJ Team
 * @since 1.0
 */
@Route("/")
public class HomeView extends Composite<AppLayout> implements DidEnterObserver {
  private static final String SPINNER_PLACEHOLDER = "<dwc-spinner></dwc-spinner>";
  private static final String EMPTY_DIRECTORY = "(empty)";
  
  private final AppLayout layout = getBoundComponent();
  private final Tree fileTree = new Tree();
  private final Editor codeEditor = new Editor();

  /**
   * Constructs the home view with file explorer layout.
   * 
   * <p>Sets up the application layout with:</p>
   * <ul>
   *   <li>Header toolbar with drawer toggle and title</li>
   *   <li>Drawer containing the file tree</li>
   *   <li>Main content area with code editor</li>
   * </ul>
   */
  public HomeView() {
    initializeLayout();
    configureEventListeners();
  }

  /**
   * Initializes the application layout components.
   */
  private void initializeLayout() {
    layout.setHeaderOffscreen(false);

    Toolbar toolbar = new Toolbar();
    toolbar.addToStart(new AppDrawerToggle());
    toolbar.addToTitle(new H1("webforJ Explorer"));
    layout.addToHeader(toolbar);

    layout.addToDrawer(fileTree);
    layout.add(codeEditor);
  }

  /**
   * Configures event listeners for tree interactions.
   */
  private void configureEventListeners() {
    fileTree.addSelectListener(this::handleTreeSelect);
    fileTree.addExpandListener(this::handleTreeExpand);
  }

  /**
   * Called when the view is entered.
   * 
   * <p>Prompts the user to select a directory to explore.</p>
   * 
   * @param event the enter event
   * @param parameters route parameters (not used)
   */
  @Override
  public void onDidEnter(DidEnterEvent event, ParametersBag parameters) {
    promptForDirectory();
  }

  /**
   * Prompts the user to select a directory to explore.
   * 
   * <p>Shows a file chooser dialog in directory selection mode.
   * If cancelled, re-prompts until a directory is selected.</p>
   */
  private void promptForDirectory() {
    FileChooserDialog dialog = new FileChooserDialog("Choose a project to open");
    dialog.setSelectionMode(FileChooserDialog.SelectionMode.DIRECTORIES);
    
    String selectedDirectory = dialog.show();
    if (selectedDirectory != null) {
      initializeTree(selectedDirectory);
    } else {
      // Re-prompt if user cancels
      promptForDirectory();
    }
  }

  /**
   * Initializes the file tree with the contents of the selected directory.
   * 
   * @param startPath the path to the root directory to display
   */
  private void initializeTree(String startPath) {
    File rootDirectory = new File(startPath);
    if (!rootDirectory.exists() || !rootDirectory.isDirectory()) {
      return;
    }

    Utility.processDirectoryContents(rootDirectory, child -> {
      TreeNode childNode = createNode(child);
      fileTree.add(childNode);
    });
  }

  /**
   * Creates a tree node for a file or directory.
   * 
   * <p>For directories, adds a spinner placeholder for lazy loading.
   * For files, sets appropriate icons based on file type.</p>
   * 
   * @param file the file or directory to create a node for
   * @return the created tree node
   */
  private TreeNode createNode(File file) {
    String displayName = file.getName().isEmpty() ? file.getAbsolutePath() : file.getName();
    TreeNode node = node(displayName);
    node.setUserData("file", file);
    node.setTooltipText(Utility.getFileTooltip(file));

    if (file.isDirectory()) {
      // Add spinner placeholder for lazy loading
      node.add(node(SPINNER_PLACEHOLDER));
    } else if (file.isFile()) {
      Utility.setFileIcons(node, file);
    }

    return node;
  }

  /**
   * Adds child nodes to a parent directory node.
   * 
   * @param parentNode the parent tree node
   * @param directory the directory whose contents to add
   */
  private void addChildrenToNode(TreeNode parentNode, File directory) {
    if (!directory.isDirectory()) {
      return;
    }
    
    File[] children = directory.listFiles();
    if (children == null || children.length == 0) {
      parentNode.add(node(EMPTY_DIRECTORY));
      return;
    }
    
    Utility.processDirectoryContents(directory, child -> {
      TreeNode childNode = createNode(child);
      parentNode.add(childNode);
    });
  }

  /**
   * Handles file selection in the tree.
   * 
   * <p>When a file is selected, loads its content into the editor
   * with appropriate syntax highlighting.</p>
   * 
   * @param event the tree selection event
   */
  private void handleTreeSelect(TreeSelectEvent event) {
    TreeNode selectedNode = event.getNode();
    Object userData = selectedNode.getUserData("file");
    
    if (!(userData instanceof File file) || !file.isFile()) {
      return;
    }
    
    try {
      String content = Utility.getFileContent(file);
      codeEditor.setValue(content);
      codeEditor.setLanguage(Utility.getFileLanguage(file.getName()));
    } catch (IOException e) {
      codeEditor.setValue("Error reading file: " + e.getMessage());
      codeEditor.setLanguage("plaintext");
    }
  }

  /**
   * Handles directory expansion in the tree.
   * 
   * <p>Implements lazy loading by replacing the spinner placeholder
   * with actual directory contents on first expansion.</p>
   * 
   * @param event the tree expand event
   */
  private void handleTreeExpand(TreeExpandEvent event) {
    TreeNode expandedNode = event.getNode();
    Object userData = expandedNode.getUserData("file");
    
    if (!(userData instanceof File file) || !file.isDirectory()) {
      return;
    }

    // Check if this is the first expansion (has spinner placeholder)
    if (expandedNode.getChildren().size() == 1 &&
        SPINNER_PLACEHOLDER.equals(expandedNode.getChildren().get(0).getText())) {
      // Remove spinner and load actual contents
      expandedNode.remove(expandedNode.getChildren().get(0));
      addChildrenToNode(expandedNode, file);
    }
  }
}
