package com.smu.urvoice.vo.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties({"owner"})
public class ChatVO {
    private int id;
    private int botId;
    private String text;

    public ChatVO(int botId, String text, int isBot, Date createDate, String owner) {
        this.botId = botId;
        this.text = text;
        this.isBot = isBot;
        this.createDate = createDate;
        this.owner = owner;
    }

    private int isBot;
    private Date createDate;

    @ApiModelProperty(hidden=true)
    private String owner;
}
