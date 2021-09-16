package com.smu.urvoice.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smu.urvoice.vo.FileVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"password"})
public class UserDetailDto {
    private String loginId;
    @ApiModelProperty(hidden=true)
    private String password;
    private FileVO imageFile;
}
