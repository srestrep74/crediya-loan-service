package co.com.crediya.loan.api.helpers;

import co.com.crediya.loan.api.helpers.ApiStandardError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ErrorResponseBuilder {

    public static Mono<ServerResponse> buildErrorResponse(
            HttpStatus status, String errorType, String message, ServerRequest request
    ) {
        return buildErrorResponse(status, errorType, message, request.path());
    }

    public static Mono<ServerResponse> buildErrorResponse(
            HttpStatus status, String errorType, String message, String path) {
        ApiStandardError apiError = new ApiStandardError(
                status.value(),
                errorType,
                message,
                path);

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiError);
    }

    public static Mono<ServerResponse> notFound(String message, String path) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                message,
                path);
    }

    public static Mono<ServerResponse> conflict(String message, String path) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "Resource Conflict",
                message,
                path);
    }

    public static Mono<ServerResponse> validationError(String message, String path) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                message,
                path);
    }

    public static Mono<ServerResponse> unauthorized(String message, String path) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                message,
                path);
    }

    public static Mono<ServerResponse> internalServerError(String message, String path) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                message,
                path);
    }

}
