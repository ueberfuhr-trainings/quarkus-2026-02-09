package de.samples.quarkus;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
@TestTransaction
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

  @Tag("API-Validation")
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
  void whenPatchCustomerState_thenGetCustomer_returnsUpdatedState() {
    // --- POST: Customer mit state "active" anlegen ---
    final var uuid =
      given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
          {
            "name": "Patch Test",
            "birthdate": "2000-01-01",
            "state": "active"
          }
          """)
        .when()
        .post("/customers")
        .then()
        .statusCode(201)
        .extract()
        .path("uuid");

    // --- PATCH: State auf "locked" ändern ---
    given()
      .contentType("application/merge-patch+json")
      .body("""
        {
          "state": "locked"
        }
        """)
      .when()
      .patch("/customers/{uuid}", uuid)
      .then()
      .statusCode(204);

    // --- GET: Prüfen, dass der State "locked" ist ---
    given()
      .accept(ContentType.JSON)
      .when()
      .get("/customers/{uuid}", uuid)
      .then()
      .statusCode(200)
      .body("state", is(equalTo("locked")));
  }

  @Tag("API-Validation")
  @Test
  void whenPatchCustomerWithInvalidState_thenReturn400() {
    // --- POST: Customer anlegen ---
    final var uuid =
      given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
          {
            "name": "Patch Validation Test",
            "birthdate": "2000-01-01",
            "state": "active"
          }
          """)
        .when()
        .post("/customers")
        .then()
        .statusCode(201)
        .extract()
        .path("uuid");

    // --- PATCH: Ungültigen State übergeben ---
    given()
      .contentType("application/merge-patch+json")
      .body("""
        {
          "state": "gelbekatze"
        }
        """)
      .when()
      .patch("/customers/{uuid}", uuid)
      .then()
      .statusCode(400);
  }

  @Test
  void whenPatchCustomerWithUnknownUuid_thenReturn404() {
    given()
      .contentType("application/merge-patch+json")
      .body("""
        {
          "state": "locked"
        }
        """)
      .when()
      .patch("/customers/{uuid}", java.util.UUID.randomUUID().toString())
      .then()
      .statusCode(404);
  }

  static Stream<Arguments> invalidCustomerBodies() {
    return Stream.of(
      // unknown / read-only properties
      Arguments.of("unbekannte Property", """
        {
          "name": "Tom Mayer",
          "birthdate": "2001-04-23",
          "state": "active",
          "gelbekatze": "meow"
        }
        """),
      Arguments.of("read-only Property uuid", """
        {
          "name": "Tom Mayer",
          "birthdate": "2001-04-23",
          "state": "active",
          "uuid": "550e8400-e29b-41d4-a716-446655440000"
        }
        """),
      // required fields
      Arguments.of("name fehlt", """
        {
          "birthdate": "2001-04-23",
          "state": "active"
        }
        """),
      Arguments.of("birthdate fehlt", """
        {
          "name": "Tom Mayer",
          "state": "active"
        }
        """),
      // name constraints: minLength 3, maxLength 100
      Arguments.of("name zu kurz", """
        {
          "name": "AB",
          "birthdate": "2001-04-23",
          "state": "active"
        }
        """),
      Arguments.of("name zu lang", """
        {
          "name": "%s",
          "birthdate": "2001-04-23",
          "state": "active"
        }
        """.formatted("A".repeat(101))),
      // birthdate format
      Arguments.of("birthdate ungültiges Format", """
        {
          "name": "Tom Mayer",
          "birthdate": "23.04.2001",
          "state": "active"
        }
        """),
      // birthdate in der Zukunft
      Arguments.of("birthdate in der Zukunft", """
        {
          "name": "Tom Mayer",
          "birthdate": "%s",
          "state": "active"
        }
        """.formatted(LocalDate.now().plusDays(1).toString())),
      // minimum age of 14
      Arguments.of("Mindestalter: 14 Jahre", """
        {
          "name": "Tom Mayer",
          "birthdate": "%s",
          "state": "active"
        }
        """.formatted(LocalDate.now().minusYears(14).plusDays(1).toString())),
      // state enum
      Arguments.of("state ungültiger Wert", """
        {
          "name": "Tom Mayer",
          "birthdate": "2001-04-23",
          "state": "gelbekatze"
        }
        """)
    );
  }

  @Tag("API-Validation")
  @ParameterizedTest(name = "Invalid Body on Create: {0}")
  @MethodSource("invalidCustomerBodies")
  void whenPostCustomerWithInvalidData_thenReturn400(String description, String body) {
    given()
      .contentType(ContentType.JSON)
      .accept(ContentType.JSON)
      .body(body)
      .when()
      .post("/customers")
      .then()
      .statusCode(400);
  }

}
