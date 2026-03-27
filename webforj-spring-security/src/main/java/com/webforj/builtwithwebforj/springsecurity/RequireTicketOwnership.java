package com.webforj.builtwithwebforj.springsecurity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom security annotation for ticket ownership verification.
 *
 * This annotation is used on route classes to enforce that:
 * - Regular users can only access tickets they own
 * - Support agents and admins can access any ticket
 *
 * The annotation specifies which route parameter contains the ticket ID.
 *
 * Example usage:
 * <pre>
 * {@code
 * @Route(value = "/tickets/:id", outlet = MainLayout.class)
 * @RequireTicketOwnership("id")
 * public class TicketDetailView extends Composite<Div> {
 *   // View implementation
 * }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireTicketOwnership {
  /**
   * The route parameter name containing the ticket ID.
   * Defaults to "id" to match the common pattern "/tickets/:id"
   */
  String value() default "id";
}
