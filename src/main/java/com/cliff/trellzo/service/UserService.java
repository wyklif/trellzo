package com.cliff.trellzo.service;

import com.cliff.trellzo.config.RabbitMqConfig;
import com.cliff.trellzo.dto.email.EmailQueueDto;
import com.cliff.trellzo.dto.requests.LoginRequestDTO;
import com.cliff.trellzo.dto.requests.UserRequestDTO;
import com.cliff.trellzo.dto.responses.TaskResponseDTO;
import com.cliff.trellzo.dto.responses.UserResponseDTO;
import com.cliff.trellzo.entity.User;
import com.cliff.trellzo.repository.UserRepository;
import com.cliff.trellzo.utils.EmailUtils;
import com.cliff.trellzo.utils.TaskUtils;
import com.cliff.trellzo.utils.UserUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserUtils userUtils;
    private final JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, RabbitTemplate rabbitTemplate, UserUtils userUtils, JwtService jwtService) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.userUtils = userUtils;
        this.jwtService = jwtService;
    }

    public List<UserResponseDTO> findAllUsers() {
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        List<User> users =   userRepository.findAll();
        for (User user : users) {
            Optional<UserResponseDTO> userResponseDTO = findUserById(user.getId());
            userResponseDTO.ifPresent(userResponseDTOS::add);
        }

        return userResponseDTOS;
    }

    public Optional<UserResponseDTO> findUserById(Long id){
        Optional<User> user =  userRepository.findById(id);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        if(user.isPresent()){
            userResponseDTO.setId(user.get().getId());
            userResponseDTO.setEmail(user.get().getEmail());
            userResponseDTO.setFirstName(user.get().getFirstName());
            userResponseDTO.setLastName(user.get().getLastName());
            List<TaskResponseDTO> tasks = new ArrayList<>();
            user.get().getTasks().forEach(task -> tasks.add(TaskUtils.createTaskResponseDTO(task)));
            userResponseDTO.setTasks(tasks);
        }

       return Optional.of(userResponseDTO);
    }

    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO){
        User user = userUtils.createUserFromDTO(userRequestDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        User saved =  userRepository.save(user);
        // publish the mail in the email queue
        String email = userRequestDTO.getEmail();

        String payload = EmailUtils.getActivationHtml(user.getVerificationCode());

        rabbitTemplate.convertAndSend(RabbitMqConfig.TRELLZO_EXCHANGE,RabbitMqConfig.EMAIL_ROUTING_KEY,new EmailQueueDto(email,"Activate your account",payload));
        userResponseDTO.setId(saved.getId());
        userResponseDTO.setEmail(saved.getEmail());
        userResponseDTO.setFirstName(saved.getFirstName());
        userResponseDTO.setLastName(saved.getLastName());
        List<TaskResponseDTO> tasks = new ArrayList<>();
        saved.getTasks().forEach(task -> tasks.add(TaskUtils.createTaskResponseDTO(task)));
        userResponseDTO.setTasks(tasks);
        return userResponseDTO;
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        User saved =  userRepository.save(user);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(saved.getId());
        userResponseDTO.setEmail(saved.getEmail());
        userResponseDTO.setFirstName(saved.getFirstName());
        userResponseDTO.setLastName(saved.getLastName());
        List<TaskResponseDTO> tasks = new ArrayList<>();
        saved.getTasks().forEach(task -> tasks.add(TaskUtils.createTaskResponseDTO(task)));
        userResponseDTO.setTasks(tasks);
        return userResponseDTO;
    }

    public String verify(LoginRequestDTO loginRequestDTO) {

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(loginRequestDTO.getUsername());
        }
        return "fail";

    }
}
