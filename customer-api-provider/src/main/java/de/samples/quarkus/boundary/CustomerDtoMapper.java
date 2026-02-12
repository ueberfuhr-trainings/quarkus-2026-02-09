package de.samples.quarkus.boundary;

import de.samples.quarkus.domain.Customer;
import de.samples.quarkus.domain.CustomerState;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class CustomerDtoMapper {

  public CustomerDto map(Customer source) {
    if (null == source) {
      return null;
    }
    return new CustomerDto()
      .setUuid(source.getUuid())
      .setName(source.getName())
      .setBirthdate(source.getBirthdate())
      .setState(mapState(source.getState()));
  }

  public CustomerState mapState(String state) {
    if (null == state) {
      return null;
    }
    return switch (state) {
      case "active" -> CustomerState.ACTIVE;
      case "locked" -> CustomerState.LOCKED;
      case "disabled" -> CustomerState.DISABLED;
      default -> throw new BadRequestException();
    };
  }

  public Customer map(CustomerDto source) {
    if (null == source) {
      return null;
    }
    return new Customer()
      .setUuid(source.getUuid())
      .setName(source.getName())
      .setBirthdate(source.getBirthdate())
      .setState(mapState(source.getState()));
  }

  public String mapState(CustomerState state) {
    if (null == state) {
      return null;
    }
    return switch (state) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

}
