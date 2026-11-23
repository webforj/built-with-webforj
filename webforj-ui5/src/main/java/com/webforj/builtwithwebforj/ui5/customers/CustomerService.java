package com.webforj.builtwithwebforj.ui5.customers;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Keeps the customers in memory, seeded with sample data.
 */
@Service
public class CustomerService {
  private final Map<Long, Customer> customers = new ConcurrentHashMap<>();
  private final AtomicLong ids = new AtomicLong();

  /**
   * Creates the service with sample customers.
   */
  public CustomerService() {
    save(new Customer("Ada Lovelace", "ada@analytical.io", Status.ACTIVE,
        LocalDate.of(2024, 3, 1), true));
    save(new Customer("Alan Turing", "alan@bletchley.dev", Status.ACTIVE,
        LocalDate.of(2023, 11, 15), false));
    save(new Customer("Grace Hopper", "grace@cobol.org", Status.PAUSED,
        LocalDate.of(2022, 6, 20), true));
    save(new Customer("Linus Torvalds", "linus@kernel.dev", Status.LEAD, null, false));
    save(new Customer("Margaret Hamilton", "margaret@apollo.space", Status.ACTIVE,
        LocalDate.of(2025, 1, 10), true));
    save(new Customer("Dennis Ritchie", "dennis@unix.net", Status.PAUSED,
        LocalDate.of(2021, 9, 5), false));
    save(new Customer("Barbara Liskov", "barbara@clu.edu", Status.ACTIVE,
        LocalDate.of(2024, 7, 22), true));
    save(new Customer("Ken Thompson", "ken@plan9.dev", Status.LEAD, null, true));
    save(new Customer("Katherine Johnson", "katherine@orbit.space", Status.ACTIVE,
        LocalDate.of(2023, 2, 14), false));
    save(new Customer("James Gosling", "james@oak.dev", Status.ACTIVE,
        LocalDate.of(2025, 5, 30), true));
    save(new Customer("Radia Perlman", "radia@spanning.net", Status.LEAD, null, false));
    save(new Customer("Tim Berners-Lee", "tim@www.org", Status.PAUSED,
        LocalDate.of(2022, 12, 1), true));
  }

  /**
   * Finds the customers whose name, email or status contains the query, ignoring case, sorted by
   * name.
   *
   * @param query the text to look for, blank for all customers
   * @return the matching customers
   */
  public List<Customer> search(String query) {
    String needle = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
    return customers.values().stream()
        .filter(c -> needle.isEmpty() || c.getName().toLowerCase(Locale.ROOT).contains(needle)
            || c.getEmail().toLowerCase(Locale.ROOT).contains(needle)
            || c.getStatus().getLabel().toLowerCase(Locale.ROOT).contains(needle))
        .sorted(Comparator.comparing(Customer::getName, String.CASE_INSENSITIVE_ORDER))
        .map(Customer::copy).toList();
  }

  /**
   * Finds a customer by id.
   *
   * @param id the id
   * @return a copy of the customer, if found
   */
  public Optional<Customer> find(long id) {
    return Optional.ofNullable(customers.get(id)).map(Customer::copy);
  }

  /**
   * Stores a customer, giving it an id when it is new.
   *
   * @param customer the customer
   * @return the stored customer's id
   */
  public long save(Customer customer) {
    if (customer.getId() == null) {
      customer.setId(ids.incrementAndGet());
    }
    customers.put(customer.getId(), customer.copy());
    return customer.getId();
  }

  /**
   * Removes a customer.
   *
   * @param id the id
   */
  public void delete(long id) {
    customers.remove(id);
  }
}
