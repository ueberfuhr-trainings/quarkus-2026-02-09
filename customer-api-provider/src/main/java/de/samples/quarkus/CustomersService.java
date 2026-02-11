package de.samples.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomersService {

  private final Map<UUID, Customer> customers = new HashMap<>();

  public Stream<Customer> getAllCustomers() {
    return customers
      .values()
      .stream();
  }

  public Optional<Customer> getCustomerById(UUID uuid) {
    return Optional
      .ofNullable(customers.get(uuid));
  }

  public Stream<Customer> getCustomersByState(CustomerState state) {
    return customers
      .values()
      .stream()
      .filter(c -> c.getState().equals(state));
  }

  public void createCustomer(@Valid Customer customer) {
    customer.setUuid(UUID.randomUUID());
    customers.put(customer.getUuid(), customer);
  }

  public long count() {
    return customers.size();
  }
}
