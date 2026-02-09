package de.samples.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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


  @Test
  void whenPostCustomers_Then_ReturnLocationHeaderToFetchNewResource() {

    // --- POST ---
    final var postResponse =
      given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
          {
            "name": "Tom Mayer",
            "birthdate": "2001-04-23",
            "state": "active"
          }
          """)
        .when()
        .post("/customers")
        .then()
        .statusCode(201)
        .contentType(ContentType.JSON)
        .body("name", is(equalTo("Tom Mayer")))
        .body("birthdate", is(equalTo("2001-04-23")))
        .body("state", is(equalTo("active")))
        .body("uuid", not(is(emptyOrNullString())))
        .header("Location", not(is(emptyOrNullString())))
        .extract()
        .response();

    // Location-Header auslesen
    final var location = postResponse.getHeader("Location");
    // UUID auslesen
    final var uuidFromBody = postResponse.path("uuid");

    // --- GET ---
    given()
      .accept(ContentType.JSON)
      .when()
      .get(location)
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("uuid", is(equalTo(uuidFromBody)))
      .body("name", is(equalTo("Tom Mayer")))
      .body("birthdate", is(equalTo("2001-04-23")))
      .body("state", is(equalTo("active")));
  }

  // Content Negotiation
  // POST customers -> GET /customers/{uuid} -> 200
  // POST customers -> GET /customers -> 200 + Kunde in Liste

}
