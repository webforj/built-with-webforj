package com.webforj.builtwithwebforj.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.webforj.builtwithwebforj.springsecurity.service.TicketService;
import com.webforj.router.NavigationContext;
import com.webforj.router.security.RouteAccessDecision;
import com.webforj.router.security.RouteSecurityContext;
import com.webforj.router.security.RouteSecurityEvaluator;
import com.webforj.router.security.SecurityEvaluatorChain;
import com.webforj.spring.security.annotation.RegisteredEvaluator;

/**
 * Security evaluator that enforces ticket ownership rules.
 *
 * Access is granted if:
 * 1. User has SUPPORT or ADMIN role (can view any ticket), OR
 * 2. User owns the ticket (created it)
 *
 * Access is denied if:
 * - User is not authenticated
 * - Ticket ID is missing or invalid
 * - Ticket doesn't exist
 * - User doesn't own the ticket and doesn't have SUPPORT/ADMIN role
 */
@RegisteredEvaluator(priority = 10)
public class TicketOwnershipEvaluator implements RouteSecurityEvaluator {

  @Autowired
  private TicketService ticketService;

  @Override
  public boolean supports(Class<?> routeClass) {
    return routeClass.isAnnotationPresent(RequireTicketOwnership.class);
  }

  @Override
  public RouteAccessDecision evaluate(Class<?> routeClass, NavigationContext context,
      RouteSecurityContext securityContext, SecurityEvaluatorChain chain) {

    // 1. Check authentication
    if (!securityContext.isAuthenticated()) {
      return RouteAccessDecision.denyAuthentication();
    }

    // 2. Get the annotation and parameter name
    RequireTicketOwnership annotation = routeClass.getAnnotation(RequireTicketOwnership.class);
    String paramName = annotation.value();

    // 3. Extract ticket ID from route parameters
    String ticketIdStr = context.getRouteParameters()
        .get(paramName)
        .orElse(null);

    if (ticketIdStr == null) {
      return RouteAccessDecision.deny("Ticket ID not found in route");
    }

    // 4. Parse ticket ID
    Long ticketId;
    try {
      ticketId = Long.parseLong(ticketIdStr);
    } catch (NumberFormatException e) {
      return RouteAccessDecision.deny("Invalid ticket ID format");
    }

    // 5. Get current username from UserDetails
    String username = securityContext.getPrincipal()
        .filter(p -> p instanceof UserDetails)
        .map(p -> ((UserDetails) p).getUsername())
        .orElse(null);

    if (username == null) {
      return RouteAccessDecision.deny("User not authenticated");
    }

    // 6. Check if user has SUPPORT or ADMIN role (automatic access)
    boolean hasPrivilegedRole = securityContext.getPrincipal()
        .filter(p -> p instanceof UserDetails)
        .map(p -> ((UserDetails) p).getAuthorities())
        .map(authorities -> authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_SUPPORT") || role.equals("ROLE_ADMIN")))
        .orElse(false);

    if (hasPrivilegedRole) {
      // Support/Admin can access any ticket - continue chain
      return chain.evaluate(routeClass, context, securityContext);
    }

    // 7. Check ticket ownership for regular users
    try {
      boolean isOwner = ticketService.isUserOwner(ticketId, username);
      if (isOwner) {
        // User owns the ticket - continue chain
        return chain.evaluate(routeClass, context, securityContext);
      } else {
        return RouteAccessDecision.deny("You do not have permission to view this ticket");
      }
    } catch (RuntimeException e) {
      // Ticket not found or other error
      return RouteAccessDecision.deny("Ticket not found");
    }
  }
}
