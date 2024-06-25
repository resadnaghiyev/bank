package com.rashad.bank.api.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pin", unique = true, nullable = false)
    private String pin;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("USER"));
//    }

//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return pin;
//    }
//
//    @Override
//    public boolean isEnabled() {
////        return Objects.equals(this.status.getName(), "Validated User");
//        return true;
//    }
}
