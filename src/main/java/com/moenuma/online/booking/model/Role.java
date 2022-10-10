package com.moenuma.online.booking.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GenerationType;

@Entity
@Table(name = "roles")
@Value.Immutable
@JsonDeserialize(as = ImmutableRole.class)
public abstract class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public abstract Integer getId();

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    public abstract ERole getName();

}
