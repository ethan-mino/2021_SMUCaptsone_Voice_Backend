package com.smu.urvoice.service.Diary;

import com.smu.urvoice.dto.diary.DiaryDetailDto;
import com.smu.urvoice.dto.diary.StatisticsDto;
import com.smu.urvoice.vo.DiaryVO;

import java.util.Date;
import java.util.List;

public interface DiaryService {
    int insertDiary(DiaryVO diaryVO);
    int deleteDiaryById(String loginId, int diaryId);
    int updateDiary(DiaryVO diaryVO);
    DiaryDetailDto getDiaryById(String loginId, int diaryId);
    List<DiaryDetailDto> getDiaryByDate(String loginId, Date createDate);
    List<StatisticsDto> getStatistics(String loginId, String stdYear, String stdMonth, String stdDate);
}
