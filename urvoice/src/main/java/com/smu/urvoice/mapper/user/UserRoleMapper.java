package com.smu.urvoice.mapper.user;

import com.smu.urvoice.dto.user.UserRoleDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {
	List<UserRoleDto> getRolesById(String email);
	int insertUserRole(UserRoleDto role);
}
