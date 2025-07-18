package com.dialogdata.main.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "newsletter")
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "subscription_date", nullable = false)
    private Instant subscriptionDate;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @NotNull
    @Column(name = "schedule_date", nullable = false)
    private Instant scheduleDate;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "repeat", nullable = false)
    private Boolean repeat = false;

    @NotNull
    @Column(name = "repeat_interval")
    private Integer repeatInterval;

}