package com.smu.urvoice.dto.diary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EmojiCategoryDetailDto {
    private int id;
    private String name;
    private String color;

    @JsonProperty("emojis")
    List<EmojiDetailDto> emojiDetailDtos;
}

