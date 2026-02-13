package de.samples.quarkus.infrastructure;

import de.samples.quarkus.test.CaptureOutput;
import de.samples.quarkus.test.CaptureOutput.CapturedOutput;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
//@EnableCustomersLogging - does not work with Quarkus
@TestProfile(EnableCustomersLogging.Profile.class)
@CaptureOutput
class CustomersEventsLoggerTests {

  @Test
  void whenCreateCustomer_thenLogEvent(CapturedOutput output) {
    var response = given()
      .contentType(ContentType.JSON)
      .body("""
            {
              "name": "Tom Mayer",
              "birthdate": "2001-04-23",
              "state": "active"
            }
        """)
      .accept(ContentType.JSON)
      .when()
      .post("/customers");
    response
      .then()
      .statusCode(201)
      .header("Location", is(notNullValue()));
    var uuid = response.body().jsonPath().getString("uuid");

    assertTrue(
      output.toString().matches("(?si).*Customer created.*" + uuid + ".*"),
      """
        Expected log output to contain 'Customer created' with id "%s"
        """.formatted(uuid)
    );

  }

}
