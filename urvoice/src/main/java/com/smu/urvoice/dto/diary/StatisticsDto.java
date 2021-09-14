package com.smu.urvoice.dto.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties({"id"})
public class StatisticsDto {
    private int id;
    @JsonProperty("stdDate")
    private Date date;

    @JsonProperty("emojis")
    private List<EmojiCntDto> emojiCntList;
}
