package com.cliff.trellzo.utils;

import com.cliff.trellzo.dto.requests.UserRequestDTO;
import com.cliff.trellzo.entity.User;

import java.time.LocalDateTime;
import java.util.Random;

public class UserUtils {
    public static String getUserNames(User user){
        return user.getFirstName()+" "+user.getLastName();
    }
    public static User createUserFromDTO(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEmail(userRequestDTO.getEmail());
        return user;
    }

    private static String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
