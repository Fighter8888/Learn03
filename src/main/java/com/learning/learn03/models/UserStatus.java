package com.learning.learn03.models;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserStatus {
    Pending,
    Approved,
    Rejected;

    @JsonCreator
    public static UserStatus fromString(String value) {
        return UserStatus.valueOf(value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase());
    }
}
