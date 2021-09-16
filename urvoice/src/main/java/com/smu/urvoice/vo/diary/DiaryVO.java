package com.smu.urvoice.vo.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"writer"})
public class DiaryVO {
    @ApiModelProperty(hidden=true)
    private int id;

    @ApiModelProperty(hidden=true)
    private String writer;

    private int emojiId;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date createDate;
}
