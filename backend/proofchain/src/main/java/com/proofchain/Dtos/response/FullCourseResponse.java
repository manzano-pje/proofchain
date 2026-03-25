package com.proofchain.Dtos.response;

import java.time.Instant;

public record FullCourseResponse (
     String name,
     String description,
     int hours,
     Instant createdAt,
     Instant updatedAt
){}
