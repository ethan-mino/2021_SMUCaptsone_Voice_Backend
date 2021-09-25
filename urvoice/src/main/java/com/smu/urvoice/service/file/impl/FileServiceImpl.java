package com.smu.urvoice.service.file.impl;

import com.smu.urvoice.mapper.file.FileMapper;
import com.smu.urvoice.service.file.FileService;
import com.smu.urvoice.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileMapper fileMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FileVO> getFileInfoByOwner(String owner) {
        return fileMapper.getFileInfoByOwner(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public FileVO getFileInfoById(String loginId, int fileId) {
        return fileMapper.getFileInfoById(loginId, fileId);
    }

    @Override
    @Transactional
    public int insertFileInfo(FileVO fileVO) {
        return fileMapper.insertFileInfo(fileVO);
    }

    @Override
    @Transactional
    public int deleteFileInfoById(int fileId) {
        return fileMapper.deleteFileInfoById(fileId);
    }
}
