package de.samples.quarkus.persistence.jpa;

import de.samples.quarkus.domain.Customer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerEntityMapper {

  public CustomerEntity map(Customer source) {
    if (null == source) {
      return null;
    }
    return new CustomerEntity()
      .setUuid(source.getUuid())
      .setName(source.getName())
      .setBirthdate(source.getBirthdate())
      .setState(source.getState());
  }

  public Customer map(CustomerEntity source) {
    if (null == source) {
      return null;
    }
    return new Customer()
      .setUuid(source.getUuid())
      .setName(source.getName())
      .setBirthdate(source.getBirthdate())
      .setState(source.getState());
  }

  public void copy(CustomerEntity source, CustomerEntity target) {
    target
      .setUuid(source.getUuid())
      .setName(source.getName())
      .setBirthdate(source.getBirthdate())
      .setState(source.getState());
  }

  public void copy(CustomerEntity source, Customer target) {
    target
      .setUuid(source.getUuid())
      .setName(source.getName())
      .setBirthdate(source.getBirthdate())
      .setState(source.getState());
  }

}
