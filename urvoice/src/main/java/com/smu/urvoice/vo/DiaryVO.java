package com.smu.urvoice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class DiaryVO {
    @ApiModelProperty(hidden=true)
    private int id;

    @ApiModelProperty(hidden=true)
    private String loginId;

    private int emojiId;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date createDate;
}
