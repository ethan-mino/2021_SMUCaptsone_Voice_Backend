package com.smu.urvoice.controller;

import com.smu.urvoice.dto.ApiResponse;
import com.smu.urvoice.vo.user.UserVO;
import com.smu.urvoice.service.file.FileService;
import com.smu.urvoice.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.smu.urvoice.util.DateUtil.dateToString;
import static com.smu.urvoice.util.FileUtil.*;

@Api(value = "File API", description = "REST API for Diary", tags = {"File"})
@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @Value("${file.upload.directory}")
    private String uploadPath;

    @ApiOperation(value = "파일 업로드")
    @PostMapping("/upload")
    public ApiResponse fileDownload(@ApiIgnore @AuthenticationPrincipal UserVO userVO,
                                    @RequestParam("file") MultipartFile multipartFile) throws Exception{
        String owner = userVO.getLoginId();

        String originalFilename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();

        String now = dateToString(new Date(), "YYYY-MM-dd HH:mm:ss");
        String newFilename = now + "_" +originalFilename;

        String savePath = uploadPath + "/" + newFilename;
        File convFile = new File(savePath);
        multipartFile.transferTo(convFile);

        FileVO fileVO = new FileVO(savePath, contentType, owner, "/uploads/" + newFilename);

        int insertCnt = fileService.insertFileInfo(fileVO);

        if (insertCnt != 0){
            Map<String, Object> resultMap = new HashMap<>();
            int fileId = fileVO.getId();
            resultMap.put("id", fileId);
            return new ApiResponse(false, "", resultMap);
        }
        else {
            return new ApiResponse(true, "파일 업로드 실패!");
        }
    }
    
    @ApiOperation(value = "파일 다운로드")
    @GetMapping("/download")
    public void fileDownload(@ApiParam(value = "파일 id", required = true, example = "1") @RequestParam("fileId") int fileId,
                             @ApiIgnore HttpServletResponse response,
                             @ApiIgnore @AuthenticationPrincipal UserVO userVO){
        String loginId = userVO.getLoginId(); // 유저의 회사명

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
