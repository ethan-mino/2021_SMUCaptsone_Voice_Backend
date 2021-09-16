package com.smu.urvoice.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smu.urvoice.vo.FileVO;
import com.smu.urvoice.vo.chat.ChatModeVO;
import com.smu.urvoice.vo.chat.VoiceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({"owner"})
public class ChatBotDetailDto {
    private int id;
    private String name;

    private ChatModeVO chatMode;
    private VoiceVO voice;
    private FileVO imageFile;

    @ApiModelProperty(hidden=true)
    private String owner;

    private String lastChat;
}
