package com.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    public String getPassword() {
        return this.password;
    }
    //You store the password (encrypted!) so users can log in later.
    //in the UserDto i have a token in loc de password, deoarece
    //token --- is used to send data back to the frontend, but i don't want to send the credentials
    //over the internet, i mean to the frontend
    //Instead, once the user logs in successfully, you generate a JWT token and send that in the response.

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
