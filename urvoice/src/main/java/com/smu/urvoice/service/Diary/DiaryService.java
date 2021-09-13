package com.smu.urvoice.service.Diary;

import com.smu.urvoice.vo.DiaryVO;

public interface DiaryService {
    int insertDiary(DiaryVO diaryVO);
    int deleteDiaryById(int diaryId);
}
