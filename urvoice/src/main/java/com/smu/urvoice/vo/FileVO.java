package com.smu.urvoice.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileVO {
    private int id;
    private String filePath;
    private String contentType;
    private String owner;
}
