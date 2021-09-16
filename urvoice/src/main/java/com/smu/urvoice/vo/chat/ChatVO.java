package com.smu.urvoice.vo.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"owner"})
public class ChatVO {
    private int id;
    private int botId;
    private String text;
    private int isBot;
    private Date createDate;

    @ApiModelProperty(hidden=true)
    private String owner;
}
