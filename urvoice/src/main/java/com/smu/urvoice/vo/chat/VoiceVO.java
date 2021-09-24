package com.smu.urvoice.vo.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"owner"})
public class VoiceVO {
    private int id;
    private String name;

    @ApiModelProperty(hidden=true)
    private String owner;
}
