package com.library.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
//this is the name of my tables --- users
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //with a generated value automatically generated when creating an entity
    private Long id;

    @Setter
    @Column(nullable = false)
    private String firstName;

    @Setter
    @Column(nullable = false)
    private String lastName;

    @Setter
    @Column(nullable = false)
    private String login;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    //You store the password (encrypted!) so users can log in later.
    //in the UserDto i have a token in loc de password, deoarece
    //token --- is used to send data back to the frontend, but i don't want to send the credentials
    //over the internet, i mean to the frontend
    //Instead, once the user logs in successfully, you generate a JWT token and send that in the response.

}
