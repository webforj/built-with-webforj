package com.webforj.builtwithwebforj.advancedrag.rag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.ai.document.Document;

/**
 * The product manual the app answers from: a few hand-written entries plus 200 generated error
 * codes that read almost the same, which is where plain vector search struggles.
 */
public final class Manual {
  private static final List<Document> WRITTEN = List.of(
      doc("E-4012", "Error E-4012 means the license token has expired. Revoke the old token in "
          + "the admin console and issue a new one."),
      doc("cancel", "To cancel a subscription, open Billing and choose End plan. Access stays "
          + "active until the current period ends."),
      doc("E-2207", "Error E-2207 appears when an upload exceeds 50 MB. Split the file or raise "
          + "the limit in Settings."),
      doc("revoke", "Tokens can be revoked at any time. A revoked token stops working within "
          + "one minute."),
      doc("refresh", "The dashboard refreshes every 30 seconds. Use the refresh button to "
          + "update it immediately."),
      doc("sso", "Single sign-on supports SAML and OpenID Connect. Configure it under "
          + "Security."),
      doc("E-1003", "Error E-1003 is a network timeout. Check the proxy settings and retry."));

  private static final String[] AREAS = {"license token", "file upload", "network connection",
      "database sync", "single sign-on", "billing payment", "email delivery", "report export",
      "user import", "backup job"};
  private static final String[] CAUSES = {"has expired", "was rejected by the server",
      "timed out", "is missing a required field", "was blocked by a policy"};
  private static final String[] FIXES = {"Retry after a few minutes.",
      "Contact an administrator.", "Check the settings page.", "Restart the job."};

  private static final List<Document> GENERATED = generate();

  private Manual() {}

  public static List<Document> all() {
    return Stream.concat(WRITTEN.stream(), GENERATED.stream()).toList();
  }

  public static List<Document> generated() {
    return GENERATED;
  }

  // 200 entries with codes from one narrow range, so the codes look alike to an embedding.
  private static List<Document> generate() {
    Random random = new Random(42);
    Set<Integer> codes = new LinkedHashSet<>();
    while (codes.size() < AREAS.length * CAUSES.length * FIXES.length) {
      int code = 4000 + random.nextInt(1000);
      // E-4012 is a hand-written entry.
      if (code != 4012) {
        codes.add(code);
      }
    }
    List<Document> docs = new ArrayList<>();
    Iterator<Integer> code = codes.iterator();
    for (String area : AREAS) {
      for (String cause : CAUSES) {
        for (String fix : FIXES) {
          String id = "E-" + code.next();
          docs.add(doc(id, "Error " + id + ": the " + area + " " + cause + ". " + fix));
        }
      }
    }
    return docs;
  }

  private static Document doc(String id, String text) {
    return Document.builder().id(id).text(text).build();
  }
}
