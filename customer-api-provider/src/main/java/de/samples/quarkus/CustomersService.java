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

  private final CustomersRepository repo;

  public Stream<Customer> getAllCustomers() {
    return repo.streamAll();
  }

  public Optional<Customer> getCustomerById(UUID uuid) {
    return repo.findByIdOptional(uuid);
  }

  public Stream<Customer> getCustomersByState(String state) {
    return repo.findByState(state);
  }

  @Transactional
  public void createCustomer(@Valid Customer customer) {
    repo.persist(customer);
  }

  public long count() {
    return repo.count();
  }
}
