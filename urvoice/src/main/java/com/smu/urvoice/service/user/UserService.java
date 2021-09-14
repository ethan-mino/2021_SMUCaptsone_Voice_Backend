package com.smu.urvoice.service.user;

import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.dto.user.UserRoleDto;

import java.util.List;

public interface UserService {
    boolean loginIdValidation(String loginId);
    int createUser(UserDto userDto);
    UserDto getUserByLoginId(String loginId);
    List<UserRoleDto> getUserRoles(String loginId);
}
