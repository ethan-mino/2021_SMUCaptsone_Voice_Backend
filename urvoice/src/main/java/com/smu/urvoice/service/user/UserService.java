package com.smu.urvoice.service.user;

import com.smu.urvoice.vo.user.UserVO;
import com.smu.urvoice.vo.user.UserRoleVO;

import java.util.List;

public interface UserService {
    boolean loginIdValidation(String loginId);
    int createUser(UserVO userVO);
    UserVO getUserByLoginId(String loginId);
    List<UserRoleVO> getUserRoles(String loginId);
}
