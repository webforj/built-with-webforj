package com.webforjexplorer.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.tree.TreeNode;
import com.webforj.utilities.Assets;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Utility class providing helper methods for file operations and UI enhancements.
 * 
 * <p>This class contains static utility methods for:</p>
 * <ul>
 *   <li>File icon mapping based on file extensions</li>
 *   <li>Programming language detection for syntax highlighting</li>
 *   <li>File content reading and metadata extraction</li>
 *   <li>File size formatting and tooltip generation</li>
 *   <li>Directory traversal and sorting logic</li>
 * </ul>
 * 
 * <p>The icon and language mappings are loaded from a JSON configuration file at startup.</p>
 * 
 * @author webforJ Team
 * @since 1.0
 */
public final class Utility {
  private static final Map<String, String> ICON_MAPPINGS = new HashMap<>();
  private static final Map<String, String> FILE_LANGUAGE_MAPPINGS = new HashMap<>();
  private static final String CONFIG_PATH = "context://config/mapping.json";
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final int BYTES_PER_KB = 1024;
  private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};

  static {
    loadMappings();
  }

  private Utility() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Loads icon and language mappings from the configuration file.
   */
  private static void loadMappings() {
    Type type = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
    Map<String, Map<String, String>> config = new Gson().fromJson(
        Assets.contentOf(Assets.resolveContextUrl(CONFIG_PATH)), type);
    ICON_MAPPINGS.putAll(config.getOrDefault("iconMappings", Collections.emptyMap()));
    FILE_LANGUAGE_MAPPINGS.putAll(config.getOrDefault("fileLanguageMappings", Collections.emptyMap()));
  }

  /**
   * Sets appropriate icons for a tree node based on the associated file's extension.
   * 
   * @param node the tree node to set icons for
   * @param file the file associated with the tree node
   */
  public static void setFileIcons(TreeNode node, File file) {
    String extension = getFileExtension(file.getName());
    String iconName = ICON_MAPPINGS.getOrDefault(extension, "file");
    
    node.setIcon(TablerIcon.create(iconName));
    node.setSelectedIcon(TablerIcon.create(iconName));
  }

  /**
   * Extracts the file extension from a filename.
   * 
   * @param fileName the name of the file
   * @return the file extension in lowercase, or empty string if no extension
   */
  private static String getFileExtension(String fileName) {
    int lastDotIndex = fileName.lastIndexOf('.');
    return (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) 
        ? fileName.substring(lastDotIndex + 1).toLowerCase() 
        : "";
  }

  /**
   * Determines the programming language for syntax highlighting based on file extension.
   * 
   * @param fileName the name of the file
   * @return the language identifier for Monaco Editor, defaults to "plaintext"
   */
  public static String getFileLanguage(String fileName) {
    String extension = getFileExtension(fileName);
    return FILE_LANGUAGE_MAPPINGS.getOrDefault(extension, "plaintext");
  }

  /**
   * Reads and returns the entire content of a file as a string.
   * 
   * @param file the file to read
   * @return the file content as a string
   * @throws IOException if an I/O error occurs reading the file
   */
  public static String getFileContent(File file) throws IOException {
    return Files.readString(file.toPath());
  }

  /**
   * Generates an HTML tooltip string containing file metadata.
   * 
   * @param file the file to generate tooltip for
   * @return HTML-formatted tooltip string with path, type, modification date, and size
   */
  public static String getFileTooltip(File file) {
    String type = file.isDirectory() ? "Directory" : "File";
    String lastModified = DATE_FORMAT.format(file.lastModified());
    String size = file.isFile() ? getFileSize(file.length()) : "N/A";
    
    return String.format(
        "<strong>Path:</strong> %s<br>" +
        "<strong>Type:</strong> %s<br>" +
        "<strong>Last Modified:</strong> %s<br>" +
        "<strong>Size:</strong> %s",
        file.getAbsolutePath(),
        type,
        lastModified,
        size);
  }

  /**
   * Formats a file size in bytes to a human-readable string.
   * 
   * @param bytes the size in bytes
   * @return formatted string (e.g., "1.5 MB", "234 B")
   */
  public static String getFileSize(long bytes) {
    if (bytes < BYTES_PER_KB) {
      return bytes + " " + SIZE_UNITS[0];
    }
    
    int unitIndex = (int) (Math.log(bytes) / Math.log(BYTES_PER_KB));
    if (unitIndex >= SIZE_UNITS.length) {
      unitIndex = SIZE_UNITS.length - 1;
    }
    
    double size = bytes / Math.pow(BYTES_PER_KB, unitIndex);
    return String.format("%.1f %s", size, SIZE_UNITS[unitIndex]);
  }

  /**
   * Processes all files in a directory with custom sorting.
   * 
   * <p>Files are sorted with the following priority:</p>
   * <ol>
   *   <li>Hidden directories (starting with '.')</li>
   *   <li>Regular directories</li>
   *   <li>Files</li>
   * </ol>
   * 
   * @param directory the directory to process
   * @param fileProcessor the consumer to process each file
   */
  public static void processDirectoryContents(File directory, Consumer<File> fileProcessor) {
    File[] children = directory.listFiles();
    if (children != null && children.length > 0) {
      Arrays.stream(children)
          .filter(File::exists)
          .sorted(Utility::compareFilesByTypeAndName)
          .forEach(fileProcessor);
    }
  }

  /**
   * Compares two files for sorting based on type and name.
   * 
   * @param file1 the first file to compare
   * @param file2 the second file to compare
   * @return negative if file1 should come before file2, positive if after, zero if equal
   */
  private static int compareFilesByTypeAndName(File file1, File file2) {
    boolean isHiddenDir1 = file1.isDirectory() && file1.getName().startsWith(".");
    boolean isHiddenDir2 = file2.isDirectory() && file2.getName().startsWith(".");
    
    // Prioritize hidden directories
    if (isHiddenDir1 && !isHiddenDir2) {
      return -1;
    }
    if (isHiddenDir2 && !isHiddenDir1) {
      return 1;
    }
    
    // Then sort directories before files
    int directoryComparison = Boolean.compare(file2.isDirectory(), file1.isDirectory());
    if (directoryComparison != 0) {
      return directoryComparison;
    }
    
    // Finally, sort by name (case-insensitive)
    return file1.getName().compareToIgnoreCase(file2.getName());
  }
}
