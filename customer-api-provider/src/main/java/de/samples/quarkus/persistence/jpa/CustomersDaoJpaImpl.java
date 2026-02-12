package de.samples.quarkus.persistence.jpa;

import de.samples.quarkus.domain.Customer;
import de.samples.quarkus.domain.CustomerState;
import de.samples.quarkus.domain.CustomersDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@Typed(CustomersDao.class)
@RequiredArgsConstructor
public class CustomersDaoJpaImpl implements CustomersDao {

  private final CustomersRepository repo;
  private final CustomerEntityMapper mapper;

  @Override
  public Stream<Customer> findAll() {
    return repo
      .streamAll()
      .map(mapper::map);
  }

  @Override
  public Optional<Customer> findById(UUID uuid) {
    return repo
      .findByIdOptional(uuid)
      .map(mapper::map);
  }

  @Override
  public Stream<Customer> findByState(CustomerState state) {
    return repo
      .findByState(state)
      .map(mapper::map);
  }

  @Transactional
  @Override
  public void save(Customer customer) {
    final var entity = mapper.map(customer);
    if (null == entity.getUuid()) {
      repo.persist(entity);
    } else {
      repo
        .findByIdOptional(entity.getUuid())
        .ifPresent(existing -> mapper.copy(entity, existing));
    }
  }

  @Override
  public long count() {
    return repo.count();
  }

}
