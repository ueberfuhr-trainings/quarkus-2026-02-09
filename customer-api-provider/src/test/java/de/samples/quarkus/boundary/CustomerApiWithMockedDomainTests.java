package de.samples.quarkus.boundary;

import de.samples.quarkus.domain.CustomersService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerApiWithMockedDomainTests {

  // service -> MOCK -> customer mit UUID existiert nicht -> API=>404
  @InjectMock
  CustomersService customersService;

  @Test
  void givenCustomerDoesNotExist_whenGetCustomer_thenReturn404() {
    final var uuid = UUID.randomUUID();

    when(customersService.getCustomerById(uuid))
      .thenReturn(Optional.empty());

    given()
      .accept(ContentType.JSON)
      .when()
      .get("/customers/{uuid}", uuid)
      .then()
      .statusCode(404);
  }

}
