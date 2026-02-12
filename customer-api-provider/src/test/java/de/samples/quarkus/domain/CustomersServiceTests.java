package de.samples.quarkus.domain;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@QuarkusTest
public class CustomersServiceTests {

  @Inject
  CustomersService customersService;
  @InjectMock
  CustomersDao customersDao;

  // Validierung der Service-Klasse
  @Test
  void givenInvalidCustomer_whenCreateCustomer_thenThrowException() {

    var customer = new Customer();
    assertThrows(
      Exception.class,
      () -> customersService.createCustomer(customer)
    );
    verifyNoInteractions(customersDao);

  }

}
