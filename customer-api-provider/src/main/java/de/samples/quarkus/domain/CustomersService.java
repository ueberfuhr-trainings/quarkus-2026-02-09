package de.samples.quarkus.domain;

import de.samples.quarkus.domain.events.CustomerCreatedEvent;
import de.samples.quarkus.domain.events.CustomerUpdatedEvent;
import de.samples.quarkus.shared.interceptors.FireEvent;
import de.samples.quarkus.shared.interceptors.LogPerformance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;

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

  public Stream<Customer> getCustomersByState(CustomerState state) {
    return dao.findByState(state);
  }

  @LogPerformance(Logger.Level.DEBUG)
  @FireEvent(CustomerCreatedEvent.class)
  public void createCustomer(
    @Valid
    @ConvertGroup(to = ValidationGroups.OnCreate.class)
    Customer customer
  ) {
    dao.save(customer);
  }

  @FireEvent(CustomerUpdatedEvent.class)
  public void updateCustomer(
    @Valid
    @ConvertGroup(to = ValidationGroups.OnUpdate.class)
    Customer customer
  ) {
    dao.save(customer);
  }

  public long count() {
    return dao.count();
  }
}
