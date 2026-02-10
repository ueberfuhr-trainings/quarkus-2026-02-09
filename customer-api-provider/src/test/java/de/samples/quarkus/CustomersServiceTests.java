package de.samples.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class CustomersServiceTests {

  @Inject
  CustomersService customersService;

  // Validierung der Service-Klasse
  @Test
  void givenInvalidCustomer_whenCreateCustomer_thenThrowException() {

    var customer = new Customer();
    assertThrows(
      Exception.class,
      () -> customersService.createCustomer(customer)
    );

  }

}
