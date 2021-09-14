package com.smu.urvoice.mapper.diary;

import com.smu.urvoice.dto.diary.StatisticsDto;
import com.smu.urvoice.vo.DiaryVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DiaryMapper {
    int insertDiary(DiaryVO diaryVO);
    int deleteDiaryById(String loginId, int diaryId);
    int updateDiary(DiaryVO diaryVO);
    DiaryVO getDiaryById(String loginId, int diaryId);
    List<DiaryVO> getDiaryByDate(String loginId, Date createDate);

    List<StatisticsDto> getStatistics(String loginId, String stdYear, String stdMonth, String stdDate, String type);
}
