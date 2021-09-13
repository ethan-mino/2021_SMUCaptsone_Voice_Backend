package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.service.Diary.DiaryService;
import com.smu.urvoice.vo.DiaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiaryController {
    @Autowired
    DiaryService diaryService;

    @PostMapping("/diary")
    public ApiResponse createDiary(@AuthenticationPrincipal UserDto userDto, @RequestBody DiaryVO diaryVO) {
        String loginId = userDto.getLoginId();
        diaryVO.setLoginId(loginId);

        diaryService.insertDiary(diaryVO);
        return new ApiResponse(null);
    }
}
