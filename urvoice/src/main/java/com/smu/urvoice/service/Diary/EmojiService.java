package com.smu.urvoice.service.Diary;

import com.smu.urvoice.dto.diary.EmojiCategoryDetailDto;
import com.smu.urvoice.dto.diary.EmojiDetailDto;

import java.util.List;

public interface EmojiService {
    EmojiDetailDto getEmojiById(int emojiId);
    List<EmojiDetailDto> getEmojis();
    List<EmojiCategoryDetailDto> getEmojiCategories();
}
