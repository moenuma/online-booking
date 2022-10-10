package com.moenuma.online.booking.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Value.Immutable
@JsonDeserialize(as = ImmutableUser.class)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public abstract Long getId();

    @NotBlank
    @Size(max = 20)
    public abstract String getUsername();

    @NotBlank
    @Size(max = 50)
    @Email
    public abstract String getEmail();

    @NotBlank
    @Size(max = 120)
    public abstract String getPassword();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public abstract Set<Role> getRoles();
}
