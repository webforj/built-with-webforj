package com.webforjrest.frontend.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.navigator.Navigator;
import com.webforj.component.table.Table;
import com.webforjrest.frontend.data.PostDelegatingRepository;
import com.webforjrest.frontend.models.Post;

/**
 * View for displaying posts from JSONPlaceholder API.
 * Demonstrates DelegatingRepository pattern with pagination.
 */
public class PostsView extends Composite<FlexLayout> {

  private final PostDelegatingRepository postRepository;

  private FlexLayout container = getBoundComponent();
  private FlexLayout header;
  private FlexLayout tableContainer;

  private H1 pageTitle;
  private Paragraph description;
  private Anchor docsLink;
  private Table<Post> postsTable;
  private Navigator navigator;

  public PostsView(PostDelegatingRepository postRepository) {
    this.postRepository = postRepository;

    initializeComponents();
    setupLayout();
  }

  private void initializeComponents() {
    pageTitle = new H1("JSONPlaceholder Posts");
    description = new Paragraph(
        "This example shows how to work with external APIs using the DelegatingRepository pattern. " +
        "Instead of manually loading data, we create a PostDelegatingRepository that handles fetching posts from " +
        "the JSONPlaceholder API (https://jsonplaceholder.typicode.com) on demand. We then use setRepository() to assign this " +
        "repository to the Table, and add a Navigator component configured for 15 items per page. " +
        "The Table and Navigator automatically coordinate fetching and displaying only the data needed for the current page, " +
        "making it efficient for working with large datasets without loading everything at once."
    );

    docsLink = new Anchor("https://docs.webforj.com/docs/advanced/repository/delegating-repository",
        "Learn more about how to manage and query collections of entities →");
    docsLink.setTarget("blank");
    docsLink.setStyle("color", "var(--dwc-color-primary)");
    docsLink.setStyle("text-decoration", "none");
    docsLink.setStyle("font-weight", "500");

    postsTable = new Table<>();
    postsTable.setRepository(postRepository);
    setupTableColumns();

    // Create paginator with 15 items per page
    navigator = new Navigator(postRepository, 15);
  }

  private void setupLayout() {
    container.setDirection(FlexDirection.COLUMN);

    header = new FlexLayout();
    header.setDirection(FlexDirection.COLUMN);
    header.setStyle("margin-bottom", "2rem");
    header.add(pageTitle, description, docsLink);

    tableContainer = new FlexLayout();
    tableContainer.setDirection(FlexDirection.COLUMN);
    tableContainer.add(postsTable, navigator);

    container.add(header, tableContainer);
  }

  private void setupTableColumns() {
    postsTable.addColumn("ID", Post::getId).setMinWidth(80);
    postsTable.addColumn("User ID", Post::getUserId).setMinWidth(100);
    postsTable.addColumn("Title", Post::getTitle).setMinWidth(200);
    postsTable.addColumn("Body", Post::getBodyTruncated);

    postsTable.addClassName("posts-table");
    postsTable.setRowHeight(45);
    postsTable.setHeight("60dvh");
  }
}
