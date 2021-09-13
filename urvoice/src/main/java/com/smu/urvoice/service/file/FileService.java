package com.smu.urvoice.service.file;

import com.smu.urvoice.vo.FileVO;

import java.util.List;

public interface FileService {
    List<FileVO> getFileInfoByOwner(String owner);
    FileVO getFileInfoById(int fileId);
    int insertFileInfo (FileVO fileVO);
    int deleteFileInfoById(int fileId);
}
