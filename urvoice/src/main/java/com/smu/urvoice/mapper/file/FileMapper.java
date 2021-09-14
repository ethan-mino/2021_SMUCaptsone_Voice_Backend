package com.smu.urvoice.mapper.file;

import com.smu.urvoice.vo.FileVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMapper {
    List<FileVO> getFileInfoByOwner(String owner);
    FileVO getFileInfoById(String loginId, int fileId);
    int insertFileInfo (FileVO fileVO);
    int deleteFileInfoById(int fileId);
}
