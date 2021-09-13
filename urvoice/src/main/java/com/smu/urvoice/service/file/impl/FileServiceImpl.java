package com.smu.urvoice.service.file.impl;

import com.smu.urvoice.mapper.file.FileMapper;
import com.smu.urvoice.service.file.FileService;
import com.smu.urvoice.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileMapper fileMapper;

    @Override
    public List<FileVO> getFileInfoByOwner(String owner) {
        return fileMapper.getFileInfoByOwner(owner);
    }

    @Override
    public FileVO getFileInfoById(int fileId) {
        return fileMapper.getFileInfoById(fileId);
    }

    @Override
    public int insertFileInfo(FileVO fileVO) {
        return fileMapper.insertFileInfo(fileVO);
    }

    @Override
    public int deleteFileInfoById(int fileId) {
        return fileMapper.deleteFileInfoById(fileId);
    }
}
