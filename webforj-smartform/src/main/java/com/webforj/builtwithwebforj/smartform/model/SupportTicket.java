package com.webforj.builtwithwebforj.smartform.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * The bean the form binds to. The model fills it, and the same constraints check typed and AI
 * values alike.
 */
public class SupportTicket {
  @NotBlank(message = "Name is required")
  private String name;

  @Email(message = "Enter a valid email address")
  @NotBlank(message = "Email is required")
  private String email;

  @Pattern(regexp = "LOW|NORMAL|HIGH", message = "Priority must be LOW, NORMAL or HIGH")
  private String priority;

  @Size(max = 200, message = "Keep the summary under 200 characters")
  private String summary;

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

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }
}
