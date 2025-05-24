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
//    User signUpToUser(SignUpDto signUpDto);
//}

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    User signUpToUser(SignUpDto signUpDto);
}





