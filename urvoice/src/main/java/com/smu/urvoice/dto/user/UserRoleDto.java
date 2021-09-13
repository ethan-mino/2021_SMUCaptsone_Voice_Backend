package com.smu.urvoice.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleDto {
	private int id;
	private String loginId;
	private String roleName;

	public UserRoleDto(String loginId, String roleName) {
		this.loginId = loginId;
		this.roleName = roleName;
	}
}