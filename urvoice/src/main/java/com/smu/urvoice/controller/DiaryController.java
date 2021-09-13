package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.vo.DiaryVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiaryController {

    @PostMapping("/diary")
    public ApiResponse createDiary(@RequestBody DiaryVO diaryVO) {
        return new ApiResponse(null);
    }
}
