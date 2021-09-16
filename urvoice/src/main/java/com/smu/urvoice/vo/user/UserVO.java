package com.smu.urvoice.vo.user;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserVO {
    private String loginId;
    private String password;
    private Integer imageFileId;
}