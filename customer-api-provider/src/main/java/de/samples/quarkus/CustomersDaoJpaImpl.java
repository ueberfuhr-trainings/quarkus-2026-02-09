package de.samples.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Typed(CustomersDao.class)
@RequiredArgsConstructor
public class CustomersDaoJpaImpl implements CustomersDao {

  private final CustomersRepository repo;

  @Override
  public Stream<Customer> findAll() {
    return repo.streamAll();
  }

  @Override
  public Optional<Customer> findById(UUID uuid) {
    return repo.findByIdOptional(uuid);
  }

  @Override
  public Stream<Customer> findByState(String state) {
    return repo.findByState(state);
  }

  @Override
  public void save(Customer customer) {
    if (null == customer.getUuid()) {
      repo.persist(customer);
    } else {
      repo
        .findByIdOptional(customer.getUuid())
        .ifPresent(entity -> {
          entity.setState(customer.getState());
          entity.setName(customer.getName());
          entity.setBirthdate(customer.getBirthdate());
        });
    }
  }

  @Override
  public long count() {
    return repo.count();
  }

}
