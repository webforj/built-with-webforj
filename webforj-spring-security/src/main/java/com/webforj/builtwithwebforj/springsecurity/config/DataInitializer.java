package com.webforj.builtwithwebforj.springsecurity.config;

import com.webforj.builtwithwebforj.springsecurity.entity.Comment;
import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketPriority;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketStatus;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketType;
import com.webforj.builtwithwebforj.springsecurity.repository.CommentRepository;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import com.webforj.builtwithwebforj.springsecurity.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes the database with sample users and tickets for demonstration.
 */
@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TicketService ticketService;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    // Create regular users (5)
    User user1 = createUser("user", "password", "Alice Johnson", "alice@example.com", "USER");
    User user2 = createUser("bob", "password", "Bob Smith", "bob@example.com", "USER");
    User user3 = createUser("charlie", "password", "Charlie Davis", "charlie@example.com", "USER");
    User user4 = createUser("diana", "password", "Diana Martinez", "diana@example.com", "USER");
    User user5 = createUser("emma", "password", "Emma Wilson", "emma@example.com", "USER");

    // Create support agents (3)
    User agent1 = createUser("agent", "password", "Support Agent Sarah", "sarah@support.com", "USER",
        "SUPPORT");
    User agent2 = createUser("mike", "password", "Support Agent Mike", "mike@support.com", "USER",
        "SUPPORT");
    User agent3 = createUser("lisa", "password", "Support Agent Lisa", "lisa@support.com", "USER",
        "SUPPORT");

    // Create admins (2)
    User admin1 = createUser("admin", "admin", "Admin James", "james@admin.com", "USER", "SUPPORT",
        "ADMIN");
    User admin2 = createUser("rachel", "admin", "Admin Rachel", "rachel@admin.com", "USER",
        "SUPPORT", "ADMIN");

    // Create sample tickets
    createSampleTickets(user1, user2, user3, agent1, admin1);

    System.out.println("Data initialization completed!");
    System.out.println("\nDemo accounts:");
    System.out.println("Regular Users (USER role):");
    System.out.println("  - user / password (Alice Johnson)");
    System.out.println("  - bob / password (Bob Smith)");
    System.out.println("  - charlie / password (Charlie Davis)");
    System.out.println("  - diana / password (Diana Martinez)");
    System.out.println("  - emma / password (Emma Wilson)");
    System.out.println("\nSupport Agents (USER + SUPPORT roles):");
    System.out.println("  - agent / password (Support Agent Sarah)");
    System.out.println("  - mike / password (Support Agent Mike)");
    System.out.println("  - lisa / password (Support Agent Lisa)");
    System.out.println("\nAdministrators (USER + SUPPORT + ADMIN roles):");
    System.out.println("  - admin / admin (Admin James)");
    System.out.println("  - rachel / admin (Admin Rachel)");
  }

  private User createUser(String username, String password, String displayName, String email,
      String... roles) {
    User user = new User(username, passwordEncoder.encode(password), displayName, email);
    for (String role : roles) {
      user.addRole(role);
    }
    return userRepository.save(user);
  }

  private void createSampleTickets(User user1, User user2, User user3, User agent1, User admin1) {
    // Ticket 1: User1's (Alice) open ticket
    Ticket ticket1 = new Ticket("TKT-001", "Login page not loading",
        "When I try to access the login page, it shows a blank screen. This started happening yesterday.",
        TicketType.BUG, TicketPriority.HIGH, user1);
    ticket1 = ticketService.createTicket(ticket1);

    // Ticket 2: User1's (Alice) in-progress ticket
    Ticket ticket2 = new Ticket("TKT-002", "Add dark mode feature",
        "It would be great to have a dark mode option in the settings. My eyes get tired from the bright screen.",
        TicketType.FEATURE_REQUEST, TicketPriority.MEDIUM, user1);
    ticket2.setStatus(TicketStatus.IN_PROGRESS);
    ticket2 = ticketService.createTicket(ticket2);
    addComment(ticket2, "Thanks for the suggestion! We're looking into this.", "agent", false);
    addComment(ticket2, "Great! Looking forward to it.", "user", false);

    // Ticket 3: User1's (Alice) closed ticket
    Ticket ticket3 = new Ticket("TKT-003", "How to reset password?",
        "I forgot my password and can't find the reset link on the login page.",
        TicketType.QUESTION, TicketPriority.LOW, user1);
    ticket3.setStatus(TicketStatus.CLOSED);
    ticket3 = ticketService.createTicket(ticket3);
    addComment(ticket3, "You can click 'Forgot Password' below the login form.", "agent", false);
    addComment(ticket3, "Found it, thanks!", "user", false);

    // Ticket 4: User2's (Bob) ticket
    Ticket ticket4 = new Ticket("TKT-004", "API documentation outdated",
        "The API documentation in the developer portal hasn't been updated for v2.0.",
        TicketType.OTHER, TicketPriority.MEDIUM, user2);
    ticket4 = ticketService.createTicket(ticket4);

    // Ticket 5: Admin1's (James) ticket
    Ticket ticket5 = new Ticket("TKT-005", "Database backup failing",
        "The automated database backup scheduled for 2AM is failing with timeout errors.",
        TicketType.BUG, TicketPriority.URGENT, admin1);
    ticket5 = ticketService.createTicket(ticket5);
    addComment(ticket5, "Investigating the backup logs now.", "admin", true); // Internal note

    // Ticket 6: User1's (Alice) ticket with internal notes
    Ticket ticket6 = new Ticket("TKT-006", "Cannot upload profile picture",
        "Every time I try to upload a profile picture, it says 'file too large' even though it's only 500KB.",
        TicketType.BUG, TicketPriority.LOW, user1);
    ticket6 = ticketService.createTicket(ticket6);
    addComment(ticket6, "Looks like a bug in the file size validator.", "agent", true); // Internal
    addComment(ticket6, "We're working on fixing this issue.", "agent", false); // Public

    // Ticket 7: User3's (Charlie) resolved ticket
    Ticket ticket7 = new Ticket("TKT-007", "Email notifications not working",
        "I'm not receiving email notifications for ticket updates.",
        TicketType.BUG, TicketPriority.MEDIUM, user3);
    ticket7.setStatus(TicketStatus.RESOLVED);
    ticket7 = ticketService.createTicket(ticket7);
    addComment(ticket7, "Fixed the SMTP configuration. Please test.", "agent", false);
    addComment(ticket7, "Working now, thank you!", "charlie", false);

    // Ticket 8: User2's (Bob) open urgent ticket
    Ticket ticket8 = new Ticket("TKT-008", "Payment gateway down",
        "Users are reporting they cannot complete purchases. Payment gateway is returning 500 errors.",
        TicketType.BUG, TicketPriority.URGENT, user2);
    ticket8 = ticketService.createTicket(ticket8);
    addComment(ticket8, "Contacted payment provider, waiting for response.", "mike", true);
  }

  private void addComment(Ticket ticket, String text, String author, boolean isInternal) {
    Comment comment = new Comment(ticket, text, author, isInternal);
    commentRepository.save(comment);
  }
}
