package com.smu.urvoice.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class FileUtil {
    public static boolean makeZipFile(List<String> fileNameList, String targetFileDirPath, String zipFileName){ // ? targetFileDirPath은 압축할 파일들의 위치 (https://pro-chef.tistory.com/15 참조)
        byte [] buf = new byte[1024];

        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFileDirPath + "/" + zipFileName))){
            for(String fileName : fileNameList){
                try(FileInputStream  fis = new FileInputStream(targetFileDirPath + "/" + fileName)){
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    out.putNextEntry(zipEntry);
                    int len;

                    while((len = fis.read(buf)) > 0){
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                }catch (IOException err){
                    err.printStackTrace();
                    return false;
                }
            }
        }catch(IOException err){
            err.printStackTrace();
            return false;
        }

        return true;
    }

    public static void sendFile(HttpServletResponse response, String contentType, String filePath){ // filePath는 파일명을 포함한 경로
        File file = new File(filePath);
        String fileName = FilenameUtils.getName(filePath);

        String encodedFileName = URLEncoder.encode(fileName).replaceAll("\\+", "%20");  // 한글 파일명을 사용하기 위해 encode
        long fileLength = file.length();

        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", Long.toString(fileLength));
        response.setHeader("pragma", "no-cache;");
        response.setHeader("Expires", "-1;");

        try(FileInputStream fis = new FileInputStream(filePath);    // 선언된 객체들에 대해서 try가 종료될 때 자동으로 자원을 해제해주는 기능 (fis.close(), out.close()하지 않아도 된다.)
            OutputStream out = response.getOutputStream();){
            int readCount = 0;
            byte[] buffer = new byte[1024];
            while((readCount = fis.read(buffer)) != -1){
                out.write(buffer, 0, readCount);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("file download Error");
        }
    }

    public static boolean removeFileList(String dirPath, List<String> fileNameList){
        boolean remove = true;  // 파일 제거 성공 여부를 나타내는 flag

        for(String filename : fileNameList){    // 전달받은 파일명 리스트에 해당하는 파일을 제거
            String filePath = dirPath + "/" + filename;
            if(!removeFile(filePath))
                remove = false; // 파일이 존재하지 않거나 파일을 삭제하지 못한 경우
        }

        return remove;
    }

    public static boolean removeFile(String filePath){
        boolean remove = true;  // 파일 제거 성공 여부를 나타내는 flag

        File file = new File(filePath);
        if(file.exists()){
            if(!file.delete())
                remove = false; // 파일을 삭제하지 못한 경우
        }else{
            remove = false; // 파일이 존재하지 않는 경우
        }

        return remove;
    }
}
