package com.webforj.builtwithwebforj.advancedrag.views;

import com.webforj.builtwithwebforj.advancedrag.rag.Trace;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;

/**
 * Shows what each stage of the pipeline did for one question. Chips are manual entries; the ones
 * the answer was built from are highlighted in every step, so you can follow them through.
 */
public class PipelinePanel extends Composite<Div> {
  private static final int SHOWN = 5;

  public PipelinePanel() {
    getBoundComponent().addClassName("pipeline");
    showEmpty("Ask a question to see each step of the pipeline.");
  }

  public void showEmpty(String message) {
    getBoundComponent().removeAll();
    getBoundComponent().add(new Paragraph(message));
  }

  public void show(String question, Trace trace, long totalMillis) {
    Set<String> used = trace.kept().stream().map(Document::getId).collect(Collectors.toSet());
    Div root = getBoundComponent();
    root.removeAll();

    root.add(step("1. Rewrite", trace.rewriteMillis(),
        line("You asked: " + question),
        line("Searched for: " + (trace.rewritten() != null ? trace.rewritten() : "not run"))));

    Div expand = step("2. Expand", trace.expandMillis());
    trace.expanded().forEach(query -> expand.add(line(query)));
    root.add(expand);

    Div search = step("3. Search by meaning and by keyword", trace.searchMillis());
    for (String query : trace.expanded()) {
      Trace.Search result = trace.search(query);
      search.add(new Div(new Span(query)).addClassName("pipeline__query"));
      search.add(chips("Meaning", result.meaning(), used));
      search.add(chips("Keyword", result.keyword(), used));
      search.add(chips("Merged", result.merged(), used));
    }
    root.add(search);

    Div rerank = step("4. Rerank", trace.rerankMillis(),
        line("Kept " + trace.kept().size() + " of " + trace.candidates().size()
            + " candidates"));
    trace.kept().forEach(doc -> rerank.add(new Div(chip(doc, used),
        new Span(doc.getText())).addClassName("pipeline__kept")));
    root.add(rerank);

    long others = trace.rewriteMillis() + trace.expandMillis() + trace.searchMillis()
        + trace.rerankMillis();
    root.add(step("5. Answer", Math.max(0, totalMillis - others),
        chips("Built from", trace.kept(), used)));
  }

  private static Div step(String title, long millis, Component... body) {
    Div header = new Div(new Span(title).addClassName("pipeline__title"),
        new Span(millis + " ms").addClassName("pipeline__time"))
        .addClassName("pipeline__header");
    Div step = new Div(header).addClassName("pipeline__step");
    step.add(body);
    return step;
  }

  private static Div line(String text) {
    return new Div(new Span(text)).addClassName("pipeline__line");
  }

  private static Div chips(String label, List<Document> docs, Set<String> used) {
    Div row = new Div(new Span(label).addClassName("pipeline__label")).addClassName("pipeline__chips");
    docs.stream().limit(SHOWN).forEach(doc -> row.add(chip(doc, used)));
    if (docs.isEmpty()) {
      row.add(new Span("none").addClassName("pipeline__time"));
    } else if (docs.size() > SHOWN) {
      row.add(new Span("+" + (docs.size() - SHOWN) + " more").addClassName("pipeline__time"));
    }
    return row;
  }

  private static Span chip(Document doc, Set<String> used) {
    Span chip = new Span(doc.getId());
    chip.addClassName("pipeline__chip");
    chip.setAttribute("title", doc.getText());
    if (used.contains(doc.getId())) {
      chip.addClassName("pipeline__chip--used");
    }
    return chip;
  }
}
