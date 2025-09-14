package com.cliff.trellzo.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    @NotEmpty(message = "The Title Field is Required")
    private String title;
    @NotEmpty(message = "The Description Field Is Required")
    private String description;
    @NotEmpty(message = "The Email Field Is Required")
    @Email
    private String email;
    @NotEmpty(message = "The Status Field Is Required")
    private String status;
    @NotEmpty(message = "The UserID Field Is Required")
    private String user_id;

    @Override
    public String toString() {
        return "TaskRequestDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
