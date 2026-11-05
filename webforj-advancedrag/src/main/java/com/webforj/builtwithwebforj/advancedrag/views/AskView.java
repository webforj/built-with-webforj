package com.webforj.builtwithwebforj.advancedrag.views;

import com.webforj.Environment;
import com.webforj.builtwithwebforj.advancedrag.service.DocsAssistant;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.card.Card;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.router.annotation.Route;
import java.util.concurrent.CompletableFuture;

@Route("/")
public class AskView extends Composite<FlexLayout> {
  private final DocsAssistant assistant;
  private final MarkdownViewer answer = new MarkdownViewer();
  private final TextField question = new TextField("Question", "What does E-4012 mean?");
  private final Button ask = new Button("Ask", ButtonTheme.PRIMARY);
  private final PipelinePanel pipeline = new PipelinePanel();
  private CompletableFuture<DocsAssistant.Answer> current;
  private boolean destroyed;

  public AskView(DocsAssistant assistant) {
    this.assistant = assistant;

    Div form = new Div(question, ask).addClassName("advancedrag__ask");
    Card card = new Card(form, answer);
    card.addToIcon(TablerIcon.create("book"));
    card.addToTitle(new Span("Product manual"));
    card.addToCaption(new Span("Try an error code like E-2207, or ask how to cancel a plan"));

    Card trace = new Card(pipeline);
    trace.addToIcon(TablerIcon.create("route"));
    trace.addToTitle(new Span("Pipeline"));
    trace.addToCaption(new Span("Highlighted entries are the ones the answer was built from"));

    Div columns = new Div(card, trace).addClassName("advancedrag__columns");

    Div header = new Div(
        new H1("Ask the manual"),
        new Paragraph("Each question is rewritten, expanded, searched by meaning and by keyword, "
            + "and reranked before the AI answers."));

    getBoundComponent().setDirection(FlexDirection.COLUMN).addClassName("advancedrag")
        .add(header, columns);
    ask.onClick(e -> run());
  }

  private void run() {
    ask.setEnabled(false);
    answer.setContent("Searching the documentation...");
    pipeline.showEmpty("Rewriting, expanding, searching and reranking...");
    String asked = question.getValue();
    current = assistant.ask(asked);
    current.whenComplete((result, error) -> Environment.runLater(() -> {
      // A view the user left gets no updates.
      if (destroyed) {
        return;
      }
      if (error == null) {
        answer.setContent(result.text());
        pipeline.show(asked, result.trace(), result.millis());
      } else {
        answer.setContent("The search failed. Try again.");
        pipeline.showEmpty("The pipeline stopped with an error.");
      }
      ask.setEnabled(true);
    }));
  }

  // Cancelling stops this view from waiting. The model calls already started still finish,
  // because cancelling a CompletableFuture doesn't interrupt the thread that runs them.
  @Override
  protected void onDestroy() {
    destroyed = true;
    if (current != null && !current.isDone()) {
      current.cancel(true);
    }
    super.onDestroy();
  }
}
