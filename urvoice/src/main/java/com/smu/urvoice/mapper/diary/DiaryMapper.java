package com.smu.urvoice.mapper.diary;

import com.smu.urvoice.dto.diary.StatisticsDto;
import com.smu.urvoice.vo.diary.DiaryVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiaryMapper {
    int insertDiary(DiaryVO diaryVO);
    int deleteDiaryById(String writer, int diaryId);
    int updateDiary(DiaryVO diaryVO);
    DiaryVO getDiaryById(String writer, int diaryId);
    List<DiaryVO> getDiaryByDate(String writer, Date createDate);

    List<StatisticsDto> getStatistics(String writer, String stdYear, String stdMonth, String stdDate, String type);
}
