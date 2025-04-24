package com.library.repository;


import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //i extend the jpa repository with the entity type and the type of the ID

    Optional<User> findByLogin (String login);
    //i create a method that would looks for a user by a login
    //but before continuing, i need a mapper with Mapstruct that would map the user entity with the user DTO
    //that would be in my com.library.mappers.UserMapper
    //and then i need a password encoder, this is very important, because
    //i avoid storing plain passwords in my database
    //=> new java class PasswordConfig in the com.library.config


}
