package tech.carrotly.restapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import tech.carrotly.restapi.model.helpers.AuthType;

@Builder
@Table(name = "users")
@Entity
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

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
}
