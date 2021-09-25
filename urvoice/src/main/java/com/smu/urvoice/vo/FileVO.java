package com.smu.urvoice.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"owner", "filePath"})
public class FileVO {
    private int id;
    private String filePath;
    private String contentType;
    private String owner;
    private String url;

    public FileVO(String filePath, String contentType, String owner, String url) {
        this.filePath = filePath;
        this.contentType = contentType;
        this.owner = owner;
        this.url = url;
    }
}
