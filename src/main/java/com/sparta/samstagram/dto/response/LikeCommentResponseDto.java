package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeCommentResponseDto {

    private Long commentId;
    private Boolean isLike;
    private int commentLikeCnt;

    @Builder
    public LikeCommentResponseDto(Long commentId,Boolean isLike,int commentLikeCnt){

        this.commentId = commentId;
        this.isLike = isLike;
        this.commentLikeCnt = commentLikeCnt;
    }

}
