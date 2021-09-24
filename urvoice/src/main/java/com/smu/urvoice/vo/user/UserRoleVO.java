package com.smu.urvoice.vo.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleVO {
	private int id;
	private String loginId;
	private String roleName;

	public UserRoleVO(String loginId, String roleName) {
		this.loginId = loginId;
		this.roleName = roleName;
	}
}