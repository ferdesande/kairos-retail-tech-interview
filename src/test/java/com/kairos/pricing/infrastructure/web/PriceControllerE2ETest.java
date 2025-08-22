package com.kairos.pricing.infrastructure.web;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Price Controller E2E Tests")
class PriceControllerE2ETest {

    private static final int VALID_PRODUCT_ID = 35455;
    private static final int VALID_BRAND_ID = 1;
    private static final String PRICE_NOT_FOUND_ERROR_MESSAGE = "No valid price found";
    private static final String NOT_FOUND_ERROR_TYPE = "Not Found";
    private static final String VALIDATION_ERROR_TYPE = "Validation Error";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = "http://localhost:" + port + "/prices";
    }

    @Test
    @DisplayName("Test 1: request at 14/06/2020 10:00 for valid product and valid brand")
    void acceptanceTest1() {
        given()
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("productId", equalTo(VALID_PRODUCT_ID))
                .body("brandId", equalTo(VALID_BRAND_ID))
                .body("priceList", equalTo(1))
                .body("startDate", equalTo("2020-06-14T00:00:00"))
                .body("endDate", equalTo("2020-12-31T23:59:59"))
                .body("price", equalTo(35.50f))
                .body("currency", equalTo("EUR"));
    }

    @Test
    @DisplayName("Test 2: request at 14/06/2020 16:00 for valid product and valid brand")
    void acceptanceTest2() {
        given()
                .param("applicationDate", "2020-06-14T16:00:00")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("productId", equalTo(VALID_PRODUCT_ID))
                .body("brandId", equalTo(VALID_BRAND_ID))
                .body("priceList", equalTo(2))
                .body("startDate", equalTo("2020-06-14T15:00:00"))
                .body("endDate", equalTo("2020-06-14T18:30:00"))
                .body("price", equalTo(25.45f))
                .body("currency", equalTo("EUR"));
    }

    @Test
    @DisplayName("Test 3: request at 14/06/2020 21:00 for valid product and valid brand")
    void acceptanceTest3() {
        given()
                .param("applicationDate", "2020-06-14T21:00:00")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("productId", equalTo(VALID_PRODUCT_ID))
                .body("brandId", equalTo(VALID_BRAND_ID))
                .body("priceList", equalTo(1))
                .body("startDate", equalTo("2020-06-14T00:00:00"))
                .body("endDate", equalTo("2020-12-31T23:59:59"))
                .body("price", equalTo(35.50f))
                .body("currency", equalTo("EUR"));
    }

    @Test
    @DisplayName("Test 4: request at 15/06/2020 10:00 for valid product and valid brand")
    void acceptanceTest4() {
        given()
                .param("applicationDate", "2020-06-15T10:00:00")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("productId", equalTo(VALID_PRODUCT_ID))
                .body("brandId", equalTo(VALID_BRAND_ID))
                .body("priceList", equalTo(3))
                .body("startDate", equalTo("2020-06-15T00:00:00"))
                .body("endDate", equalTo("2020-06-15T11:00:00"))
                .body("price", equalTo(30.50f))
                .body("currency", equalTo("EUR"));
    }

    @Test
    @DisplayName("Test 5: request at 16/06/2020 21:00 for valid product and valid brand")
    void acceptanceTest5() {
        given()
                .param("applicationDate", "2020-06-16T21:00:00")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body("productId", equalTo(VALID_PRODUCT_ID))
                .body("brandId", equalTo(VALID_BRAND_ID))
                .body("priceList", equalTo(4))
                .body("startDate", equalTo("2020-06-15T16:00:00"))
                .body("endDate", equalTo("2020-12-31T23:59:59"))
                .body("price", equalTo(38.95f))
                .body("currency", equalTo("EUR"));
    }

    // Hint: The following tests are not required for the test interview, but they cover input parameter validation
    // and price not found scenarios

    @Test
    @DisplayName("return 404 when product does not exist")
    void returnNotFoundWhenProductDoesNotExist() {
        given()
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", 99999)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                .body("type", equalTo(NOT_FOUND_ERROR_TYPE))
                .body("detail", containsString(PRICE_NOT_FOUND_ERROR_MESSAGE))
                .body("status", equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    @DisplayName("return 404 when brand does not exist")
    void returnNotFoundWhenBrandDoesNotExist() {
        given()
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", 999)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                .body("type", equalTo(NOT_FOUND_ERROR_TYPE))
                .body("detail", containsString(PRICE_NOT_FOUND_ERROR_MESSAGE))
                .body("status", equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    @DisplayName("return 404 when date is outside all price ranges")
    void returnNotFoundWhenDateOutsideAllRanges() {
        given()
                .param("applicationDate", "2019-01-01T10:00:00")  // Date before all ranges
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                .body("type", equalTo(NOT_FOUND_ERROR_TYPE))
                .body("detail", containsString(PRICE_NOT_FOUND_ERROR_MESSAGE))
                .body("status", equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    @DisplayName("return 400 when applicationDate is missing")
    void returnBadRequestWhenApplicationDateIsMissing() {
        given()
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("type", equalTo(VALIDATION_ERROR_TYPE))
                .body("detail", equalTo("PriceRequest is invalid: Application date is required"))
                .body("status", equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("return 400 when applicationDate has invalid format")
    void returnBadRequestWhenApplicationDateHasInvalidFormat() {
        given()
                .param("applicationDate", "invalid-date-format")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", VALID_BRAND_ID)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("type", equalTo(VALIDATION_ERROR_TYPE))
                .body("detail", equalTo("Invalid format for parameter 'applicationDate'"))
                .body("status", equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    @NullSource
    @DisplayName("return 400 when productId is invalid")
    void returnBadRequestWhenProductIdIsInvalid(Long productId) {
        given()
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("brandId", VALID_BRAND_ID)
                .param("productId", productId)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("type", equalTo(VALIDATION_ERROR_TYPE))
                .body("detail", equalTo("PriceRequest is invalid: Product ID must be positive"))
                .body("status", equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    @NullSource
    @DisplayName("return 400 when brandId is invalid")
    void returnBadRequestWhenBrandIdIsInvalid(Long brandId) {
        given()
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", VALID_PRODUCT_ID)
                .param("brandId", brandId)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("type", equalTo(VALIDATION_ERROR_TYPE))
                .body("detail", containsString("PriceRequest is invalid: Brand ID must be positive"))
                .body("status", equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("return 400 with multiple validation errors")
    void returnBadRequestWithMultipleValidationErrors() {
        given()
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", -1)    // Invalid
                .param("brandId", 0)       // Invalid
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("type", equalTo(VALIDATION_ERROR_TYPE))
                .body("detail", equalTo(
                        """
                                PriceRequest is invalid:
                                - Product ID must be positive
                                - Brand ID must be positive
                                """.trim()))
                .body("status", equalTo(HttpStatus.SC_BAD_REQUEST));
    }
}