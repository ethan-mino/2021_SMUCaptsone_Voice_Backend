package com.smu.urvoice.mapper.user;

import com.smu.urvoice.vo.user.UserVO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
	UserVO getUserByLoginId(String loginId);
	int insertUser(UserVO userVO);
}
