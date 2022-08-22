package com.sparta.samstagram.dto.response;

import com.sparta.samstagram.domain.PostLike;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LikePostResponseDto {
    private Long postId;
    private Boolean isLike;
    private int postLikeCnt;

    @Builder
    public LikePostResponseDto(Long postId,Boolean isLike,int postLikeCnt){

        this.postId = postId;
        this.isLike = isLike;
        this.postLikeCnt = postLikeCnt;
    }
}
