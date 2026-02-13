package de.samples.quarkus.persistence.inmemory;

import de.samples.quarkus.domain.Customer;
import de.samples.quarkus.domain.CustomersDao;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@DefaultBean
@ApplicationScoped
@Typed(CustomersDao.class)
public class CustomersDaoInMemoryImpl
  implements CustomersDao {

  private final Map<UUID, Customer> customers = new ConcurrentHashMap<>();

  @Override
  public Stream<Customer> findAll() {
    return customers
      .values()
      .stream();
  }

  @Override
  public Optional<Customer> findById(UUID uuid) {
    return Optional
      .ofNullable(customers.get(uuid));
  }

  @Override
  public void save(Customer customer) {
    customer.setUuid(UUID.randomUUID());
    customers.put(customer.getUuid(), customer);
  }

  @Override
  public long count() {
    return customers.size();
  }
}
