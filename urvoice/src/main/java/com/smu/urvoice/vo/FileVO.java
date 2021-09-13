package com.smu.urvoice.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileVO {
    private int id;
    private String filePath;
    private String contentType;
    private String owner;
}