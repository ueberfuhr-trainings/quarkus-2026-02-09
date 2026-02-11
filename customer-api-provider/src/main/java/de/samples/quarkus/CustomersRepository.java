package de.samples.quarkus;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class CustomersRepository
  implements PanacheRepositoryBase<Customer, UUID> {

  public Stream<Customer> findByState(String state) {
    return stream("state", state);
  }

}
