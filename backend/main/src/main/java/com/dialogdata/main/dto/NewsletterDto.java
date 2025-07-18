package com.dialogdata.main.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.dialogdata.main.entity.Newsletter}
 */
@Value
public class NewsletterDto implements Serializable {

    Integer id;

    @NotNull
    Instant subscriptionDate;

    @NotNull
    String content;

    @NotNull
    Instant scheduleDate;

    @NotNull
    Boolean repeat;

    Integer repeatInterval;
}