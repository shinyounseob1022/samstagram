package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long postId;
    private Long commentId;
    private String author;
    private String authorImgUrl;
    private String content;
    private Long commentLikeCnt;
    private Boolean isEditMode;
    private Boolean isLike;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @Builder
    public CommentResponseDto(Long postId,Long commentId,String author, String authorImgUrl,
                           String content, Long commentLikeCnt, Boolean isEditMode, Boolean isLike,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.postId = postId;
        this.commentId = commentId;
        this.author = author;
        this.authorImgUrl = authorImgUrl;
        this.content = content;
        this.commentLikeCnt = commentLikeCnt;
        this.isEditMode =isEditMode;
        this.isLike = isLike;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public CommentResponseDto() {}
}