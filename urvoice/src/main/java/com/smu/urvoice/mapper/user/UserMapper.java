package com.smu.urvoice.mapper.user;

import com.smu.urvoice.dto.user.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
	UserDto getUser(String id);
	int insertUser(UserDto userDto);
}
