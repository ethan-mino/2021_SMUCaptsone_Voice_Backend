package com.smu.urvoice.mapper.diary;

import com.smu.urvoice.dto.diary.EmojiCategoryDetailDto;
import com.smu.urvoice.dto.diary.EmojiDetailDto;
import com.smu.urvoice.vo.diary.EmojiCategoryVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmojiMapper {
    List<EmojiCategoryVO> getAllEmojiCategory();
    EmojiDetailDto getEmojiById(int emojiId);
    List<EmojiDetailDto> getEmojis();
    List<EmojiCategoryDetailDto> getEmojiCategories();
}
