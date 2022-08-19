package com.sparta.samstagram.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String content;

    @Builder
    public PostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public PostRequestDto() {}
}
