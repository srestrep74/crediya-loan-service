package co.com.crediya.loan.consumer;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

class UserServiceAdapterTest {

    private static UserServiceAdapter userServiceAdapter;
    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var webClient = WebClient.builder()
                .baseUrl(mockBackEnd.url("/").toString())
                .build();
        userServiceAdapter = new UserServiceAdapter(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("existsById should return true when user exists")
    void existsByIdReturnsTrue() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"exists\": true}"));

        var response = userServiceAdapter.existsById(123L);

        StepVerifier.create(response)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("existsById should return false when user does not exist")
    void existsByIdReturnsFalse() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"exists\": false}"));

        var response = userServiceAdapter.existsById(456L);

        StepVerifier.create(response)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("existsByIdFallback should return false on error")
    void existsByIdFallbackReturnsFalse() {
        var response = userServiceAdapter.existsByIdFallback(789L, new RuntimeException("boom"));

        StepVerifier.create(response)
                .expectNext(false)
                .verifyComplete();
    }
}
