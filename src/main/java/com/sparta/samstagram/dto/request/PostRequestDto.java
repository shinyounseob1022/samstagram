package com.sparta.samstagram.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostRequestDto {
    private String content;

    @Builder
    public PostRequestDto(String content) {
        this.content = content;
    }
    public PostRequestDto() {}
}
