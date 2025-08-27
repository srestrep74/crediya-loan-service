package co.com.crediya.loan.api.config;

import co.com.crediya.loan.api.exception.GlobalExceptionHandler;
import co.com.crediya.loan.model.exception.InvalidLoanRequestDataException;
import co.com.crediya.loan.model.exception.InvalidLoanTypeDataException;
import co.com.crediya.loan.model.exception.LoanTypeNotFoundException;
import co.com.crediya.loan.model.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;

import java.util.Map;

@Configuration
public class ExceptionConfig {

    @Bean
    public Map<Class<? extends Exception>, HttpStatus> exceptionToStatusCode() {
        return Map.of(
                InvalidLoanRequestDataException.class, HttpStatus.BAD_REQUEST,
                InvalidLoanTypeDataException.class, HttpStatus.BAD_REQUEST,
                LoanTypeNotFoundException.class, HttpStatus.NOT_FOUND,
                UserNotFoundException.class, HttpStatus.NOT_FOUND,
                ConstraintViolationException.class, HttpStatus.BAD_REQUEST,
                IllegalArgumentException.class, HttpStatus.BAD_REQUEST
        );
    }

    @Bean
    public HttpStatus defaultStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Bean
    @Order(-2)
    public GlobalExceptionHandler globalExceptionHandler(
            WebProperties webProperties,
            ApplicationContext applicationContext,
            ServerCodecConfigurer configurer
    ) {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(
                new DefaultErrorAttributes(),
                webProperties.getResources(),
                applicationContext,
                exceptionToStatusCode(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        globalExceptionHandler.setMessageWriters(configurer.getWriters());
        globalExceptionHandler.setMessageReaders(configurer.getReaders());

        return globalExceptionHandler;
    }
}
