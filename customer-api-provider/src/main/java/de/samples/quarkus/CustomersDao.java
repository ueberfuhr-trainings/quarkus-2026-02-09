package de.samples.quarkus;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Data Access Object for {@link Customer} entities.
 * Persistence providers must implement this interface
 * to integrate with the business logic layer.
 */
public interface CustomersDao {

  /**
   * Returns all customers.
   *
   * @return a stream of all customers
   */
  Stream<Customer> findAll();

  /**
   * Finds a customer by its unique identifier.
   *
   * @param uuid the unique identifier
   * @return the customer, or empty if not found
   */
  default Optional<Customer> findById(UUID uuid) {
    return findAll()
      .filter(c -> uuid.equals(c.getUuid()))
      .findFirst();
  }

  /**
   * Finds all customers matching the given state.
   * The default implementation filters {@link #findAll()} in memory.
   * Persistence providers should override this method for better performance.
   *
   * @param state the state to filter by
   * @return a stream of matching customers
   */
  default Stream<Customer> findByState(String state) {
    return findAll()
      .filter(c -> state.equals(c.getState()));
  }

  /**
   * Persists a new customer.
   *
   * @param customer the customer to persist
   */
  void save(Customer customer);

  /**
   * Returns the total number of customers.
   * The default implementation counts the elements from {@link #findAll()}.
   * Persistence providers should override this method for better performance.
   *
   * @return the total number of customers
   */
  default long count() {
    return findAll()
      .count();
  }

}
