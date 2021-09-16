package com.smu.urvoice.mapper.user;

import com.smu.urvoice.dto.user.UserDetailDto;
import com.smu.urvoice.vo.user.UserVO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
	UserDetailDto getUserDetailByLoginId(String loginId);
	UserVO getUserByLoginId(String loginId);
	int insertUser(UserVO userVO);
}
