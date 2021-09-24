package com.smu.urvoice.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"owner"})
public class ChatDetailDto {
    private int id;
    private int chatBotId;
    private String text;
    private Boolean isBot;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date createDate;

    @ApiModelProperty(hidden=true)
    private String owner;
    private Map<String, Object> Sender;
}
