package com.smu.urvoice.service.Diary;

import com.smu.urvoice.dto.diary.EmojiCategoryDetailDto;
import com.smu.urvoice.dto.diary.EmojiDetailDto;
import com.smu.urvoice.vo.EmojiCategoryVO;

import java.util.List;

public interface EmojiService {
    List<EmojiCategoryVO> getAllEmojiCategory();
    EmojiDetailDto getEmojiById(int emojiId);
    List<EmojiDetailDto> getEmojis();
    List<EmojiCategoryDetailDto> getEmojiCategories();
}
