package com.smu.urvoice.dto.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"writer"})
public class DiaryDetailDto {
    private int id;
    private String content;

    @ApiModelProperty(hidden=true)
    private String writer;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date createDate;
    @JsonProperty("emoji")
    EmojiDetailDto emojiDetailDto;
}
