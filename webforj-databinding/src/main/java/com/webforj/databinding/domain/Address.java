package com.webforj.databinding.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Address {

  @NotBlank(message = "Street is required")
  @Size(max = 80, message = "Street is too long")
  private String street = "";

  @NotBlank(message = "City is required")
  @Size(max = 60, message = "City is too long")
  private String city = "";

  @NotBlank(message = "Postal code is required")
  @Size(max = 12, message = "Postal code is too long")
  private String postalCode = "";

  @NotBlank(message = "Country is required")
  private String country = "";

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
