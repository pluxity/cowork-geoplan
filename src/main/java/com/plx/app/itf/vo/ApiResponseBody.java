package com.plx.app.itf.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseBody {

    HttpStatus result;
    String message;

    @Builder
    public ApiResponseBody(HttpStatus result, String message) {
        this.result = result;
        this.message = message;
    }

}
