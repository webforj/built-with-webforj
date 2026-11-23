package com.webforj.builtwithwebforj.ui5.customers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * A customer of the company.
 */
public class Customer {
  private Long id;

  @NotBlank(message = "Name is required")
  private String name = "";

  @NotBlank(message = "Email is required")
  @Email(message = "Enter a valid email address")
  private String email = "";

  @NotNull(message = "Status is required")
  private Status status = Status.LEAD;

  private LocalDate contractStart;

  private boolean newsletter;

  /**
   * Creates an empty customer.
   */
  public Customer() {}

  /**
   * Creates a customer.
   *
   * @param name the name
   * @param email the email
   * @param status the status
   * @param contractStart the contract start
   * @param newsletter whether the customer gets the newsletter
   */
  public Customer(String name, String email, Status status, LocalDate contractStart,
      boolean newsletter) {
    this.name = name;
    this.email = email;
    this.status = status;
    this.contractStart = contractStart;
    this.newsletter = newsletter;
  }

  /**
   * Creates a copy, so a form can edit it without touching the stored customer.
   *
   * @return the copy
   */
  public Customer copy() {
    Customer copy = new Customer(name, email, status, contractStart, newsletter);
    copy.setId(id);
    return copy;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public LocalDate getContractStart() {
    return contractStart;
  }

  public void setContractStart(LocalDate contractStart) {
    this.contractStart = contractStart;
  }

  public boolean isNewsletter() {
    return newsletter;
  }

  public void setNewsletter(boolean newsletter) {
    this.newsletter = newsletter;
  }
}
