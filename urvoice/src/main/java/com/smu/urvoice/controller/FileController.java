package com.smu.urvoice.controller;

import com.smu.urvoice.dto.user.UserDto;
import com.smu.urvoice.service.file.FileService;
import com.smu.urvoice.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

import static com.smu.urvoice.util.FileUtil.removeFile;
import static com.smu.urvoice.util.FileUtil.sendFile;

@Api(value = "File API", description = "REST API for Diary", tags = {"File"})
@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @ApiOperation(value = "파일 다운로드", tags = {"File"})
    @GetMapping("/download")
    public void fileDownload(@ApiParam(value = "파일 id", required = true, example = "1") @RequestParam("fileId") int fileId,
                             @ApiIgnore HttpServletResponse response,
                             @ApiIgnore @AuthenticationPrincipal UserDto userDto){
        String loginId = userDto.getLoginId(); // 유저의 회사명

        FileVO fileInfoVo = fileService.getFileInfoById(loginId, fileId);   // fileId로 파일 정보 조회

        if(fileInfoVo != null) {  // companyNm과 file의 owner 비교 (owner라면 파일 전송)
            String filePath = fileInfoVo.getFilePath();
            String contentType = fileInfoVo.getContentType();

            sendFile(response, contentType, filePath);    // 요청한 파일 전송
            removeFile(fileInfoVo.getFilePath());  // 전송한 파일 삭제
            fileService.deleteFileInfoById(fileId);   // 파일 정보 삭제
        }else {

        }
    }
}
