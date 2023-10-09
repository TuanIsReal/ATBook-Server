package com.tuanisreal.cache;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import com.tuanisreal.controller.BaseAPI;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class APIData {
    String requestId;
    BaseAPI api;
    long startTime;
}
