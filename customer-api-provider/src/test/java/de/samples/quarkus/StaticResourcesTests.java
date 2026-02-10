package de.samples.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

@QuarkusTest
class StaticResourcesTests {

  // GET /openapi.yml

  @Test
  void whenGetOpenApiYml_thenReturn200() {
    when()
      .get("/openapi.yml")
      .then()
      .statusCode(200);
  }

}
