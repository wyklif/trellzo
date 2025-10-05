package com.cliff.trellzo.utils;

import com.cliff.trellzo.dto.requests.UserRequestDTO;
import com.cliff.trellzo.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class UserUtils {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    public static String getUserNames(User user){
        return user.getFirstName()+" "+user.getLastName();
    }
    public  User createUserFromDTO(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEmail(userRequestDTO.getEmail());
        user.setUsername(userRequestDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
        return user;
    }

    private static String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
