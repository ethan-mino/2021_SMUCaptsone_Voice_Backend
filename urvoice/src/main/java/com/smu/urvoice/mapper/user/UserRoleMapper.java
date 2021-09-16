package com.smu.urvoice.mapper.user;

import com.smu.urvoice.vo.user.UserRoleVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {
	List<UserRoleVO> getRolesById(String email);
	int insertUserRole(UserRoleVO role);
}
