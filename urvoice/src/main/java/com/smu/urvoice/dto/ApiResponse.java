package com.smu.urvoice.dto;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean error;
    private String errorMessage;
    private Map<String, Object> result;

    public ApiResponse(boolean error, String errorMessage){
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public ApiResponse(Map<String, Object> result){
        this.result = result;
    }
}