package de.samples.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
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
  @Test
  void whenGetCustomers_andAcceptXml_thenReturn406() {
    given()
      .accept(ContentType.XML)
      .when()
      .get("/customers")
      .then()
      .statusCode(406);
  }

  // state-Parameter? -> active/locked/disabled / gelbekatze

  private static final Map<String, String> OTHER_STATE = Map.of(
    "active", "locked",
    "locked", "disabled",
    "disabled", "active"
  );

  @ParameterizedTest
  @ValueSource(strings = {"active", "locked", "disabled"})
  void whenPostCustomerWithState_thenFilterByMatchingState_containsCustomer(String state) {
    // --- POST ---
    final var uuid =
      given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
          {
            "name": "State Test",
            "birthdate": "2000-01-01",
            "state": "%s"
          }
          """.formatted(state))
        .when()
        .post("/customers")
        .then()
        .statusCode(201)
        .extract()
        .path("uuid");

    // --- GET mit passendem State ---
    given()
      .accept(ContentType.JSON)
      .queryParam("state", state)
      .when()
      .get("/customers")
      .then()
      .statusCode(200)
      .body("uuid", hasItem(equalTo(uuid)));
  }

  @ParameterizedTest
  @ValueSource(strings = {"active", "locked", "disabled"})
  void whenPostCustomerWithState_thenFilterByOtherState_doesNotContainCustomer(String state) {
    // --- POST ---
    final var uuid =
      given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
          {
            "name": "State Test",
            "birthdate": "2000-01-01",
            "state": "%s"
          }
          """.formatted(state))
        .when()
        .post("/customers")
        .then()
        .statusCode(201)
        .extract()
        .path("uuid");

    // --- GET mit anderem State ---
    given()
      .accept(ContentType.JSON)
      .queryParam("state", OTHER_STATE.get(state))
      .when()
      .get("/customers")
      .then()
      .statusCode(200)
      .body("uuid", not(hasItem(equalTo(uuid))));
  }

  @Test
  void whenGetCustomersWithInvalidState_thenReturn400() {
    given()
      .accept(ContentType.JSON)
      .queryParam("state", "gelbekatze")
      .when()
      .get("/customers")
      .then()
      .statusCode(400);
  }

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

  // POST customers -> GET /customers -> 200 + Kunde in Liste
  @Test
  void whenPostCustomer_thenGetCustomers_containsNewCustomer() {
    // --- POST ---
    final var uuidFromBody =
      given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
          {
            "name": "Lisa Schmidt",
            "birthdate": "1995-07-15",
            "state": "active"
          }
          """)
        .when()
        .post("/customers")
        .then()
        .statusCode(201)
        .extract()
        .path("uuid");

    // --- GET /customers ---
    given()
      .accept(ContentType.JSON)
      .when()
      .get("/customers")
      .then()
      .statusCode(200)
      .contentType(ContentType.JSON)
      .body("uuid", org.hamcrest.Matchers.hasItem(equalTo(uuidFromBody)));
  }

  // GET /customers/{uuid} mit unbekannter UUID -> 404
  @Test
  void whenGetCustomerByUnknownUuid_thenReturn404() {
    given()
      .accept(ContentType.JSON)
      .when()
      .get("/customers/{uuid}", java.util.UUID.randomUUID().toString())
      .then()
      .statusCode(404);
  }

  @Test
  void whenPostCustomerWithUnknownProperty_thenReturn400() {
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body("""
        {
          "name": "Tom Mayer",
          "birthdate": "2001-04-23",
          "state": "active",
          "gelbekatze": "meow"
        }
        """)
      .when()
      .post("/customers")
      .then()
      .statusCode(400);
  }

  @Test
  void whenPostCustomerWithReadOnlyUuid_thenReturn400() {
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body("""
        {
          "name": "Tom Mayer",
          "birthdate": "2001-04-23",
          "state": "active",
          "uuid": "550e8400-e29b-41d4-a716-446655440000"
        }
        """)
      .when()
      .post("/customers")
      .then()
      .statusCode(400);
  }

}
