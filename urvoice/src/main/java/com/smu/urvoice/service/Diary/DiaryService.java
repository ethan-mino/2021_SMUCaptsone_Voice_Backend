package com.smu.urvoice.service.Diary;

import com.smu.urvoice.dto.diary.DiaryDetailDto;
import com.smu.urvoice.dto.diary.StatisticsDto;
import com.smu.urvoice.vo.diary.DiaryVO;

import java.util.Date;
import java.util.List;

public interface DiaryService {
    int insertDiary(DiaryVO diaryVO);
    int deleteDiaryById(String writer, int diaryId);
    int updateDiary(DiaryVO diaryVO);
    DiaryDetailDto getDiaryById(String writer, int diaryId);
    List<DiaryDetailDto> getDiaryByDate(String writer, Date createDate);
    List<StatisticsDto> getStatistics(String writer, String stdYear, String stdMonth, String stdDate);
}
