package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MypageResponseDto {
    private String nickname;
    private String memberImgUrl;

    @Builder
    public MypageResponseDto(String nickname, String memberImgUrl) {
        this.nickname = nickname;
        this.memberImgUrl = memberImgUrl;
    }

    public MypageResponseDto() {}
}
