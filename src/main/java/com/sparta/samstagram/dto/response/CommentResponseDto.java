package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponseDto {
    private Long id;
    private String author;
    private String authorImgUrl;
    private String content;
    private Long commentLikeCnt;
    private Boolean isEditMode;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @Builder
    public CommentResponseDto(Long id,String author, String authorImgUrl,
                           String content, Long commentLikeCnt, Boolean isEditMode,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.author = author;
        this.authorImgUrl = authorImgUrl;
        this.content = content;
        this.commentLikeCnt = commentLikeCnt;
        this.isEditMode =isEditMode;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public CommentResponseDto() {}
}