package com.webforj.databind.service;

import com.webforj.databind.domain.Address;
import com.webforj.databind.domain.Employee;
import com.webforj.databind.domain.EmergencyContact;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory employee store. webforJ is running on plain Jetty here (no Spring DI), so the
 * service is exposed as a process-wide singleton via {@link #getInstance()}.
 */
public final class EmployeeService {

  private static final EmployeeService INSTANCE = new EmployeeService();

  private final List<Employee> employees = new ArrayList<>();

  private EmployeeService() {
    seed();
  }

  public static EmployeeService getInstance() {
    return INSTANCE;
  }

  public List<Employee> findAll() {
    return new ArrayList<>(employees);
  }

  public void save(Employee employee) {
    employees.add(employee);
  }

  private void seed() {
    employees.add(buildEmployee(
        "Ada", "Lovelace", "ada@example.com", "Engineer",
        "12 Mathematical Way", "London", "W1A 1AA", "United Kingdom",
        "Charles Babbage", "Mentor", "+44 20 7946 0991"));

    employees.add(buildEmployee(
        "Alan", "Turing", "alan@example.com", "Engineer",
        "27 Bletchley Park Rd", "Milton Keynes", "MK3 6EB", "United Kingdom",
        "Joan Clarke", "Friend", "+44 19 0864 0404"));

    employees.add(buildEmployee(
        "Katherine", "Johnson", "katherine@example.com", "Designer",
        "1 NASA Way", "Hampton", "23681", "United States",
        "Dorothy Vaughan", "Friend", "+1 757 555 0142"));
  }

  private static Employee buildEmployee(
      String firstName, String lastName, String email, String role,
      String street, String city, String postalCode, String country,
      String contactName, String relationship, String phone) {
    Employee e = new Employee();
    e.setFirstName(firstName);
    e.setLastName(lastName);
    e.setEmail(email);
    e.setRole(role);

    Address address = e.getAddress();
    address.setStreet(street);
    address.setCity(city);
    address.setPostalCode(postalCode);
    address.setCountry(country);

    EmergencyContact contact = e.getEmergencyContact();
    contact.setContactName(contactName);
    contact.setRelationship(relationship);
    contact.setPhone(phone);
    return e;
  }
}
