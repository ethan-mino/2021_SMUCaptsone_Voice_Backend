package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String USERNAME_ALREADY_EXIST = "이미 가입된 회원입니다.";
    private final String SIGN_UP_FAIL = "회원가입에 실패하였습니다.";

    @GetMapping("/username/validation")
    public ApiResponse usernameValidation(@RequestParam String loginId){
        boolean validation = userService.loginIdValidation(loginId);	// 아이디 중복 검사

        return (validation) ? new ApiResponse(false, "") : new ApiResponse(true, USERNAME_ALREADY_EXIST);
    }

    @PostMapping("/signUp")
    public ApiResponse signUp(@RequestBody UserDto userDto){
        String username = userDto.getLoginId();

        boolean validation = userService.loginIdValidation(username); // 아이디 중복 검사

        if(validation){
            String password = userDto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);

            userDto.setPassword(encodedPassword);

            int createCnt = userService.createUser(userDto);

            return (createCnt == 1) ? new ApiResponse(false, "") : new ApiResponse(true, SIGN_UP_FAIL);
        }else{
            return new ApiResponse(true, USERNAME_ALREADY_EXIST);
        }
    }
}