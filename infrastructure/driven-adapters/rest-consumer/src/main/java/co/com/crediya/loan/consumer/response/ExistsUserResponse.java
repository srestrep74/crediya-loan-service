package co.com.crediya.loan.consumer.response;

import lombok.Builder;

@Builder
public record ExistsUserResponse(
        Boolean exists,
        Long userId
) {}
