package com.terraViva.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Builder
@AllArgsConstructor
//@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Role", length = 5)
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String firstName;
    private String lastName;

    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Transient
    private String role;

    public User() {
        setRoleBasedOnType();
    }

    private void setRoleBasedOnType() {
        if (this instanceof Administrator) {
            this.role = "ROLE_ADMIN";
        } else if (this instanceof Professor) {
            this.role = "ROLE_PROF";
        } else {
            this.role = "ROLE_STUD";
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //String role;
        if (this instanceof Administrator) {
            role = Role.ROLE_ADMIN.name();
        } else if (this instanceof Professor) {
            role = Role.ROLE_PROF.name();
        } else {
            role = Role.ROLE_STUD.name();
        }
        setRoleBasedOnType();
        return List.of(() -> role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
