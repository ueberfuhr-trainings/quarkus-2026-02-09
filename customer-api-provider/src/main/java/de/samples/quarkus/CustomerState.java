package de.samples.quarkus;

import jakarta.ws.rs.BadRequestException;

public enum CustomerState {

  active,
  locked,
  disabled;

  public static CustomerState fromString(String value) {
    try {
      return valueOf(value);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid state: " + value);
    }
  }

}
