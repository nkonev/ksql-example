package com.example.kafkotest;

import java.time.LocalDateTime;

public record UserDto (
    Long userId,
    LocalDateTime registertime,
    String gender,
    String regionid
){ }