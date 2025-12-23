package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.builtwithwebforj.springsecurity.entity.Comment;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexLayout;

import java.time.format.DateTimeFormatter;

/**
 * Component for displaying a single comment or internal note.
 */
public class CommentCard extends Composite<Div> {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

  public CommentCard(Comment comment) {
    Div container = getBoundComponent();
    container.addClassName("comment-card");

    if (comment.isInternal()) {
      container.addClassName("internal");
    }

    // Top row with timestamp on the right
    FlexLayout topRow = FlexLayout.create()
        .horizontal()
        .align().center()
        .justify().between()
        .build();

    Span timestamp = new Span(comment.getCreatedAt().format(DATE_FORMATTER));
    timestamp.addClassName("comment-timestamp");

    Span authorName = new Span(comment.getAuthor());
    authorName.addClassName("comment-author");

    if (comment.isInternal()) {
      Span internalBadge = new Span("Internal Note");
      internalBadge.addClassName("internal-badge");
      topRow.add(internalBadge, timestamp);
      container.add(topRow, authorName);
    } else {
      topRow.add(authorName, timestamp);
      container.add(topRow);
    }

    // Comment text
    Paragraph text = new Paragraph(comment.getText());
    text.addClassName("comment-text");

    container.add(text);
  }
}
