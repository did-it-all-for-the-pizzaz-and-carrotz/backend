package tech.carrotly.restapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.carrotly.restapi.model.helpers.AuthType;
import tech.carrotly.restapi.model.helpers.Role;

import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.isNull;

@Builder
@Table(name = "users")
@Entity
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String surname;
    private String password;

    @Enumerated(EnumType.STRING)
    private AuthType authType = AuthType.JWT;

    private String scientificTitle;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(isNull(role))
            return Collections.singleton(new SimpleGrantedAuthority("USER"));
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
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
