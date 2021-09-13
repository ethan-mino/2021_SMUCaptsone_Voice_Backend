package com.smu.urvoice.mapper.diary;

import com.smu.urvoice.vo.DiaryVO;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryMapper {
    int insertDiary(DiaryVO diaryVO);
    int deleteDiaryById(int diaryId);
}
