package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(value = "Authentication", description = "REST API for Authentication", tags = {"Authentication"})
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String USERNAME_ALREADY_EXIST = "이미 가입된 회원입니다.";
    private final String SIGN_UP_FAIL = "회원가입에 실패하였습니다.";

    @ApiOperation(value = "아이디 중복 확인", tags = {"Authentication"})
    @GetMapping("/username/validation")
    public ApiResponse usernameValidation(@ApiParam(value = "로그인 아이디", required = true, example = "sangmyung") @RequestParam String loginId){
        boolean validation = userService.loginIdValidation(loginId);	// 아이디 중복 검사

        return (validation) ? new ApiResponse(false, "") : new ApiResponse(true, USERNAME_ALREADY_EXIST);
    }

    @ApiOperation(value = "회원가입", tags = {"Authentication"})
    @PostMapping("/signUp")
    public ApiResponse signUp(@ApiParam(value = "로그인 정보", required = true) @RequestBody UserDto userDto){
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