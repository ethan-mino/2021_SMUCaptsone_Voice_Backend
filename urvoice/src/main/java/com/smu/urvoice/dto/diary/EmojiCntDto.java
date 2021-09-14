package com.smu.urvoice.dto.diary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"id"})
public class EmojiCntDto {
    private int id;
    private int cnt;

    @JsonProperty("emoji")
    private EmojiDetailDto emojiDetailDto;
}
