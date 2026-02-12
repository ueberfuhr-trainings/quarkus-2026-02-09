package de.samples.quarkus.persistence.jpa;

import de.samples.quarkus.domain.CustomerState;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomersRepository
  implements PanacheRepositoryBase<CustomerEntity, UUID> {

  public Stream<CustomerEntity> findByState(CustomerState state) {
    return stream("state", state);
  }

}
