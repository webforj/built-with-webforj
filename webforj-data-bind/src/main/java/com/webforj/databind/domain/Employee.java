package com.webforj.databind.domain;

import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Employee {

  @NotBlank(message = "First name is required")
  @Size(max = 40, message = "First name is too long")
  private String firstName = "";

  @NotBlank(message = "Last name is required")
  @Size(max = 40, message = "Last name is too long")
  private String lastName = "";

  @NotBlank(message = "Email is required")
  @Email(message = "Enter a valid email address")
  private String email = "";

  @NotBlank(message = "Role is required")
  private String role = "";

  @Embedded
  @Valid
  private Address address = new Address();

  @Embedded
  @Valid
  private EmergencyContact emergencyContact = new EmergencyContact();

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public EmergencyContact getEmergencyContact() {
    return emergencyContact;
  }

  public void setEmergencyContact(EmergencyContact emergencyContact) {
    this.emergencyContact = emergencyContact;
  }

  public String fullName() {
    return firstName + " " + lastName;
  }
}
