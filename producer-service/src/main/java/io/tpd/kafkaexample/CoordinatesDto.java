package io.tpd.kafkaexample;

import java.math.BigDecimal;

public record CoordinatesDto(
        BigDecimal latitude,
        BigDecimal longitude
) {
}
