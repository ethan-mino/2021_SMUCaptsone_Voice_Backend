package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.dto.diary.DiaryDetailDto;
import com.smu.urvoice.dto.diary.StatisticsDto;
import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.service.Diary.DiaryService;
import com.smu.urvoice.vo.DiaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api("다이어리")
@RestController
public class DiaryController {
    @Autowired
    DiaryService diaryService;

    @ApiOperation(value = "작성일로 다이어리 조회")
    @GetMapping("/diary")
    public List<DiaryDetailDto> getDiaries(@AuthenticationPrincipal UserDto userDto, @RequestParam("stdDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date stdDate) {
        String loginId = userDto.getLoginId();

        return diaryService.getDiaryByDate(loginId, stdDate);
    }

    @ApiOperation(value = "diary Id로 다이어리 조회")
    @GetMapping("/diary/{id}")
    public DiaryDetailDto getDiary(@AuthenticationPrincipal UserDto userDto, @PathVariable("id") int diaryId) {
        String loginId = userDto.getLoginId();

        return diaryService.getDiaryById(loginId, diaryId);
    }

    @ApiOperation(value = "다이어리 생성")
    @PostMapping("/diary")
    public ApiResponse createDiary(@AuthenticationPrincipal UserDto userDto, @RequestBody DiaryVO diaryVO) {
        String loginId = userDto.getLoginId();
        diaryVO.setLoginId(loginId);

        int insertCnt = diaryService.insertDiary(diaryVO);
        if (insertCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "다이어리 추가 실패!");
    }

    @ApiOperation(value = "다이어리 삭제")
    @DeleteMapping("/diary")
    public ApiResponse deleteDiary(@AuthenticationPrincipal UserDto userDto, @RequestParam int diaryId) {
        String loginId = userDto.getLoginId();

        int deleteCnt = diaryService.deleteDiaryById(loginId, diaryId);

        if (deleteCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "다이어리 삭제 실패!");
    }

    @ApiOperation(value = "다이어리 수정")
    @PutMapping("/diary")
    public ApiResponse updateDiary(@AuthenticationPrincipal UserDto userDto, @RequestBody DiaryVO diaryVO) {
        String loginId = userDto.getLoginId();
        diaryVO.setLoginId(loginId);

        int updateCnt = diaryService.updateDiary(diaryVO);

        if (updateCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "다이어리 수정 실패!");
    }

    @ApiOperation(value = "통계 조회")
    @GetMapping("/diary/statistics")
    public List<StatisticsDto> getStatistics(@AuthenticationPrincipal UserDto userDto,
                                             @RequestParam String stdYear, @RequestParam(required = false) String stdMonth, @RequestParam(required = false) String stdDate) {

        String loginId = userDto.getLoginId();

        return diaryService.getStatistics(loginId, stdYear, stdMonth, stdDate);
    }
}
