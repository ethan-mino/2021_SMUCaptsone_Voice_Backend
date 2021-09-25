package com.smu.urvoice.service.Diary.impl;

import com.smu.urvoice.dto.diary.DiaryDetailDto;
import com.smu.urvoice.dto.diary.EmojiDetailDto;
import com.smu.urvoice.dto.diary.StatisticsDto;
import com.smu.urvoice.mapper.diary.DiaryMapper;
import com.smu.urvoice.service.Diary.DiaryService;
import com.smu.urvoice.service.Diary.EmojiService;
import com.smu.urvoice.vo.diary.DiaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {
    @Autowired
    DiaryMapper diaryMapper;

    @Autowired
    EmojiService emojiService;

    @Override
    @Transactional
    public int insertDiary(DiaryVO diaryVO) {
        return diaryMapper.insertDiary(diaryVO);
    }

    @Override
    @Transactional
    public int deleteDiaryById(String writer, int diaryId) {
        return diaryMapper.deleteDiaryById(writer, diaryId);
    }

    @Override
    @Transactional
    public int updateDiary(DiaryVO diaryVO){
        return diaryMapper.updateDiary(diaryVO);
    }

    public List<DiaryDetailDto> getDiaryByDate(String writer, Date createDate){
        List<DiaryVO> diaryVOList = diaryMapper.getDiaryByDate(writer, createDate);

        List<DiaryDetailDto> diaryDetailDtoList = new ArrayList<>();

        for (DiaryVO diaryVO : diaryVOList){
            int diaryId = diaryVO.getId();
            DiaryDetailDto diaryDetailDto = getDiaryById(writer, diaryId);
            diaryDetailDtoList.add(diaryDetailDto);
        }

        return diaryDetailDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public DiaryDetailDto getDiaryById(String writer, int diaryId) {
        DiaryVO diaryVo = diaryMapper.getDiaryById(writer, diaryId);
        if (diaryVo != null){
            int emojiId = diaryVo.getEmojiId();
            EmojiDetailDto emojiDetailDto = emojiService.getEmojiById(emojiId);
            DiaryDetailDto diaryDetailDto = new DiaryDetailDto();

            diaryDetailDto.setEmojiDetailDto(emojiDetailDto);
            diaryDetailDto.setContent(diaryVo.getContent());
            diaryDetailDto.setId(diaryVo.getId());
            diaryDetailDto.setCreateDate(diaryVo.getCreateDate());

            return diaryDetailDto;
        }else{
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<StatisticsDto> getStatistics(String writer, String stdYear, String stdMonth, String stdDate){
        List<StatisticsDto> statisticsDtoList = null;
        String searchType = "";

        if(stdMonth != null && stdDate != null){ // 해당 일의 통계 조회
            searchType = "date";
        }else if(stdMonth == null && stdDate != null){
            return statisticsDtoList;
        }else if(stdMonth != null && stdDate == null){ // 해당 년도의 일별 통계 조회
            searchType = "month";
        }else if(stdMonth == null && stdDate == null){  // 해당 년도 월별 통계 조회
            searchType = "year";
        }

        statisticsDtoList = diaryMapper.getStatistics(writer, stdYear, stdMonth, stdDate, searchType);

        return statisticsDtoList;
    }
}
