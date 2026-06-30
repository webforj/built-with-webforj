package com.webforj.databind.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Embeddable
public class EmergencyContact {

  @NotBlank(message = "Contact name is required")
  @Size(max = 80, message = "Name is too long")
  private String contactName = "";

  @NotBlank(message = "Relationship is required")
  private String relationship = "";

  @NotBlank(message = "Phone is required")
  @Pattern(regexp = "^[+()\\d\\s-]{7,20}$", message = "Enter a valid phone number")
  private String phone = "";

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getRelationship() {
    return relationship;
  }

  public void setRelationship(String relationship) {
    this.relationship = relationship;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
