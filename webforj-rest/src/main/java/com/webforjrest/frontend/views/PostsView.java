package com.webforjrest.frontend.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.H1;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.navigator.Navigator;
import com.webforj.component.table.Table;
import com.webforj.router.annotation.Route;
import com.webforjrest.frontend.data.PostDelegatingRepository;
import com.webforjrest.frontend.models.Post;

/**
 * View for displaying posts from JSONPlaceholder API.
 * Demonstrates DelegatingRepository pattern with pagination.
 */
@Route("/posts")
public class PostsView extends Composite<FlexLayout> {

  private final PostDelegatingRepository postRepository;

  private FlexLayout container = getBoundComponent();
  private FlexLayout header;
  private FlexLayout tableContainer;

  private H1 pageTitle;
  private Table<Post> postsTable;
  private Navigator navigator;

  public PostsView(PostDelegatingRepository postRepository) {
    this.postRepository = postRepository;

    initializeComponents();
    setupLayout();
  }

  private void initializeComponents() {
    pageTitle = new H1("JSONPlaceholder Posts");

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

    tableContainer = new FlexLayout();
    tableContainer.setDirection(FlexDirection.COLUMN);
    Anchor customerLink = new Anchor("/", "Back to Customer Management");
    header.add(pageTitle, customerLink);
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
    postsTable.setHeight("50dvh");
  }
}
