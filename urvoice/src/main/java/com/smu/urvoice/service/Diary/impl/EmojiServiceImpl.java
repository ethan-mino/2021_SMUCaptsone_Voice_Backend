package com.smu.urvoice.service.Diary.impl;

import com.smu.urvoice.dto.diary.EmojiDetailDto;
import com.smu.urvoice.mapper.diary.EmojiMapper;
import com.smu.urvoice.service.Diary.EmojiService;
import com.smu.urvoice.vo.EmojiCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmojiServiceImpl implements EmojiService {
    @Autowired
    EmojiMapper emojiMapper;

    @Override
    public List<EmojiCategoryVO> getAllEmojiCategory() {
        return emojiMapper.getAllEmojiCategory();
    }

    @Override
    public EmojiDetailDto getEmojiById(int emojiId) {
        return emojiMapper.getEmojiById(emojiId);
    }
}
