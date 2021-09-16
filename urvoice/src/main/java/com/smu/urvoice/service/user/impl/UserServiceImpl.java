package com.smu.urvoice.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import com.smu.urvoice.dto.user.UserDetailDto;
import com.smu.urvoice.vo.user.UserVO;
import com.smu.urvoice.vo.user.UserRoleVO;
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
		UserVO userVO = userMapper.getUserByLoginId(loginId);

		return (userVO == null);
	}

	@Transactional(readOnly = false)
	public int createUser(UserVO userVO) {
		userMapper.insertUser(userVO);
		String loginId = userVO.getLoginId();

		UserRoleVO userRoleVO = new UserRoleVO(loginId, "ROLE_USER");

		return userRoleMapper.insertUserRole(userRoleVO);
	}

	@Override
	public UserDetailDto getUserDetailByLoginId(String loginId) {
		return userMapper.getUserDetailByLoginId(loginId);
	}

	@Override
	public UserVO getUserByLoginId(String loginId) {
		return userMapper.getUserByLoginId(loginId);
	}

	@Override
	public List<UserRoleVO> getUserRoles(String loginId) {
		List<UserRoleVO> userRoleVOS = userRoleMapper.getRolesById(loginId);
		List<UserRoleVO> list = new ArrayList<>();

		for (UserRoleVO userRoleVO : userRoleVOS) {
			list.add(new UserRoleVO(loginId, userRoleVO.getRoleName()));
		}
		return list;
	}
}
