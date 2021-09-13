package com.smu.urvoice.mapper.diary;

import com.smu.urvoice.vo.EmojiCategoryVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmojiMapper {
    List<EmojiCategoryVO> getAllEmojiCategory();
}
