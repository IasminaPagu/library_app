package com.library.mappers;

import com.library.dtos.UserDto;
import com.library.model.User;
import org.mapstruct.Mapper;
import com.library.dtos.SignUpDto;
import org.mapstruct.Mapping;

//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    UserDto toUserDto(User user);
//
//    @Mapping(source = "firstName", target = "firstName")
//    @Mapping(source = "lastName", target = "lastName")
//    @Mapping(source = "login", target = "login")
//    @Mapping(target = "password", ignore = true) // still set in service manually
//    @Mapping(target = "id", ignore = true)       // Hibernate will handle this
//    User signUpToUser(SignUpDto signUpDto);
//}

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User signUpToUser(SignUpDto signUpDto);
}

//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    UserDto toUserDto(User user);
//
//    @Mapping(source = "firstName", target = "firstName")
//    @Mapping(source = "lastName", target = "lastName")
//    @Mapping(source = "login", target = "login")
//    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    User signUpToUser(SignUpDto signUpDto);
//}




