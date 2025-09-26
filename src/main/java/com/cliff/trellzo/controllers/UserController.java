package com.cliff.trellzo.controllers;

import com.cliff.trellzo.dto.requests.UserRequestDTO;
import com.cliff.trellzo.dto.responses.UserResponseDTO;
import com.cliff.trellzo.entity.User;
import com.cliff.trellzo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {

        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return userService.findUserById(id).map(u -> ResponseEntity.ok().body(u)).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO){
        return ResponseEntity.ok().body(userService.saveUser(userRequestDTO));
    }
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO){
     return ResponseEntity.ok().body(userService.updateUser(id,userRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
