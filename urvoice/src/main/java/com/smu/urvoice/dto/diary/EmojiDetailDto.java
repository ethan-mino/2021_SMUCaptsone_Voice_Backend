package com.smu.urvoice.dto.diary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class EmojiDetailDto {
    private int id;
    private int categoryId;
    private String text;
    private String color;
    private String categoryName;
}
