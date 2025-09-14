package com.cliff.trellzo.utils;

import com.cliff.trellzo.dto.requests.UserRequestDTO;
import com.cliff.trellzo.entity.User;

public class UserUtils {
    public static String getUserNames(User user){
        return user.getFirstName()+" "+user.getLastName();
    }
    public static User createUserFromDTO(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        return user;
    }
}
