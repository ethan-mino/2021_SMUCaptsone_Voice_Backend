package com.smu.urvoice.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.dto.user.UserRoleDto;
import com.smu.urvoice.mapper.user.UserMapper;
import com.smu.urvoice.mapper.user.UserRoleMapper;
import com.smu.urvoice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final UserRoleMapper userRoleMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper) {
		this.userMapper = userMapper;
		this.userRoleMapper = userRoleMapper;
	}

	@Transactional(readOnly = true)
	public boolean loginIdValidation(String loginId){
		UserDto userDto = userMapper.getUser(loginId);

		return (userDto == null);
	}

	@Transactional(readOnly = false)
	public int createUser(UserDto userDto) {
		userMapper.insertUser(userDto);
		String loginId = userDto.getLoginId();

		UserRoleDto userRoleDto = new UserRoleDto(loginId, "ROLE_USER");

		return userRoleMapper.insertUserRole(userRoleDto);
	}

	@Override
	public UserDto getUser(String loginId) {
		UserDto userDto = userMapper.getUser(loginId);

		if (userDto == null)
			return null;

		return userDto;
	}

	@Override
	public List<UserRoleDto> getUserRoles(String loginId) {
		List<UserRoleDto> userRoleDtos = userRoleMapper.getRolesById(loginId);
		List<UserRoleDto> list = new ArrayList<>();

		for (UserRoleDto userRoleDto : userRoleDtos) {
			list.add(new UserRoleDto(loginId, userRoleDto.getRoleName()));
		}
		return list;
	}
}
