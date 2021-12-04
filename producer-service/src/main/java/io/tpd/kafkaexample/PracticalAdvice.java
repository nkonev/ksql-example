package io.tpd.kafkaexample;

import java.time.LocalDateTime;

public record PracticalAdvice (
    String identifier,
    String message,
    LocalDateTime datetime
) {}