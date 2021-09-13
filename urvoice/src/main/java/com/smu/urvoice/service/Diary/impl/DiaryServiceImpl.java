package com.smu.urvoice.service.Diary.impl;

import com.smu.urvoice.mapper.diary.DiaryMapper;
import com.smu.urvoice.service.Diary.DiaryService;
import com.smu.urvoice.vo.DiaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiaryServiceImpl implements DiaryService {
    @Autowired
    DiaryMapper diaryMapper;

    @Override
    public int insertDiary(DiaryVO diaryVO) {
        return diaryMapper.insertDiary(diaryVO);
    }

    @Override
    public int deleteDiaryById(int diaryId) {
        return diaryMapper.deleteDiaryById(diaryId);
    }
}
