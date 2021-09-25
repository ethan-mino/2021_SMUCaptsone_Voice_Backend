package com.smu.urvoice.service.Diary.impl;

import com.smu.urvoice.dto.diary.EmojiCategoryDetailDto;
import com.smu.urvoice.dto.diary.EmojiDetailDto;
import com.smu.urvoice.mapper.diary.EmojiMapper;
import com.smu.urvoice.service.Diary.EmojiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmojiServiceImpl implements EmojiService {
    @Autowired
    EmojiMapper emojiMapper;

    @Override
    @Transactional(readOnly = true)
    public EmojiDetailDto getEmojiById(int emojiId) {
        return emojiMapper.getEmojiById(emojiId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmojiDetailDto> getEmojis(){
        return emojiMapper.getEmojis();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmojiCategoryDetailDto> getEmojiCategories(){
        return emojiMapper.getEmojiCategories();
    }
}
