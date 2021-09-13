package com.smu.urvoice.dto.user;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String loginId;
    private String password;
}