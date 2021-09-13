package com.smu.urvoice.controller;

import com.smu.urvoice.dto.user.CustomUserDetails;
import com.smu.urvoice.service.file.FileService;
import com.smu.urvoice.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static com.smu.urvoice.util.FileUtil.removeFile;
import static com.smu.urvoice.util.FileUtil.sendFile;

@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @GetMapping("/download")
    public void fileDownload(@RequestParam("fileId") int fileId, HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails){
        String loginId = userDetails.getUsername(); // 유저의 회사명

        FileVO fileInfoVo = fileService.getFileInfoById(fileId);   // fileId로 파일 정보 조회

        if(fileInfoVo.getOwner().equals(loginId)) {  // companyNm과 file의 owner 비교 (owner라면 파일 전송)
            String filePath = fileInfoVo.getFilePath();
            String contentType = fileInfoVo.getContentType();

            sendFile(response, contentType, filePath);    // 요청한 파일 전송
            removeFile(fileInfoVo.getFilePath());  // 전송한 파일 삭제
            fileService.deleteFileInfoById(fileId);   // 파일 정보 삭제
        }
    }
}
