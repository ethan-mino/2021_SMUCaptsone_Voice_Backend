package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.dto.diary.DiaryDetailDto;
import com.smu.urvoice.dto.diary.EmojiCategoryDetailDto;
import com.smu.urvoice.dto.diary.EmojiDetailDto;
import com.smu.urvoice.dto.diary.StatisticsDto;
import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.service.Diary.DiaryService;
import com.smu.urvoice.service.Diary.EmojiService;
import com.smu.urvoice.vo.DiaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

@Api(value = "감정일기 API", description = "REST API for Diary", tags = {"Diary"})
@RestController
public class DiaryController {
    @Autowired
    DiaryService diaryService;

    @Autowired
    EmojiService emojiService;

    @ApiOperation(value = "작성일로 다이어리 조회", tags = {"Diary"})
    @GetMapping("/diary")
    public List<DiaryDetailDto> getDiaries(@ApiIgnore @AuthenticationPrincipal UserDto userDto,
                                           @ApiParam(value = "기준일", required = true, example = "2020-09-23") @RequestParam("stdDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date stdDate) {
        String loginId = userDto.getLoginId();

        return diaryService.getDiaryByDate(loginId, stdDate);
    }

    @ApiOperation(value = "diary Id로 다이어리 조회")
    @GetMapping("/diary/{id}")
    public DiaryDetailDto getDiary(@ApiIgnore @AuthenticationPrincipal UserDto userDto,
                                   @ApiParam(value = "다이어리 id", required = true, example = "1") @PathVariable("id") int diaryId) {
        String loginId = userDto.getLoginId();

        return diaryService.getDiaryById(loginId, diaryId);
    }

    @ApiOperation(value = "다이어리 생성")
    @PostMapping("/diary")
    public ApiResponse createDiary(@ApiIgnore @AuthenticationPrincipal UserDto userDto,
                                   @ApiParam(value = "다이어리 정보", required = true) @RequestBody DiaryVO diaryVO) {
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
    public ApiResponse deleteDiary(@ApiIgnore @AuthenticationPrincipal UserDto userDto,
                                   @ApiParam(value = "다이어리 id", required = true, example = "1") @RequestParam int diaryId) {
        String loginId = userDto.getLoginId();

        int deleteCnt = diaryService.deleteDiaryById(loginId, diaryId);

        if (deleteCnt != 0)
            return new ApiResponse(false);
        else
            return new ApiResponse(true, "다이어리 삭제 실패!");
    }

    @ApiOperation(value = "다이어리 수정")
    @PutMapping("/diary")
    public ApiResponse updateDiary(@ApiIgnore @AuthenticationPrincipal UserDto userDto,
                                   @ApiParam(value = "다이어리 정보", required = true) @RequestBody DiaryVO diaryVO) {
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
    public List<StatisticsDto> getStatistics(@ApiIgnore @AuthenticationPrincipal UserDto userDto,
                                             @ApiParam(value = "기준년도", required = true, example = "2020") @RequestParam String stdYear,
                                             @ApiParam(value = "기준월", required = false, example = "08") @RequestParam(required = false) String stdMonth,
                                             @ApiParam(value = "기준일", required = false, example = "23") @RequestParam(required = false) String stdDate) {

        String loginId = userDto.getLoginId();

        return diaryService.getStatistics(loginId, stdYear, stdMonth, stdDate);
    }

    @ApiOperation("이모지 조회")
    @GetMapping("/diary/emojis")
    public List<EmojiDetailDto> getEmojis() {
        return emojiService.getEmojis();
    }

    @ApiOperation("이모지 카테고리 조회")
    @GetMapping("/diary/emoji/categories")
    public List<EmojiCategoryDetailDto> getEmojiCategories(){
        return emojiService.getEmojiCategories();
    }

}
