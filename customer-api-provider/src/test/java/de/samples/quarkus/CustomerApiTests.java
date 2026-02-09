package de.samples.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;

@QuarkusTest
class CustomerApiTests {

  // GET /customers + Accept: JSON -> 200 + JSON + Body = Liste/Array (mit Kundendaten)
  @Test
  void whenGetCustomers_thenReturn200_andJsonArray() {
    given()
      .accept(ContentType.JSON)
      .when()
      .get("/customers")
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("", instanceOf(List.class));
  }

  // GET /customers + Accept: XML -> 406

  // state-Parameter? -> active/locked/disabled / gelbekatze

}
