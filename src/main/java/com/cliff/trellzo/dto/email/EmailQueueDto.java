package com.cliff.trellzo.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EmailQueueDto {
    private String email;
    private String subject;
    private String payload;
}
