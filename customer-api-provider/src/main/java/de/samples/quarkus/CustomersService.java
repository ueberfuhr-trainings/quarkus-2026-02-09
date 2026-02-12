package de.samples.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class CustomersService {

  private final CustomersDao dao;

  public Stream<Customer> getAllCustomers() {
    return dao.findAll();
  }

  public Optional<Customer> getCustomerById(UUID uuid) {
    return dao.findById(uuid);
  }

  public Stream<Customer> getCustomersByState(String state) {
    return dao.findByState(state);
  }

  @Transactional
  public void createCustomer(@Valid Customer customer) {
    dao.save(customer);
  }

  public long count() {
    return dao.count();
  }
}
