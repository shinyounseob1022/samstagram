package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long postId;
    private String author;
    private String authorImgUrl;
    private String content;
    private String imgUrl;
    private Long postLikeCnt;
    private Long commentCnt;
    private boolean isEditMode;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public PostResponseDto(Long postId, String author, String authorImgUrl, String content, String imgUrl,
                           Long postLikeCnt, Long commentCnt, boolean isEditMode,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.postId = postId;
        this.author = author;
        this.authorImgUrl = authorImgUrl;
        this.content = content;
        this.imgUrl = imgUrl;
        this.postLikeCnt = postLikeCnt;
        this.commentCnt = commentCnt;
        this.isEditMode = isEditMode;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PostResponseDto() {}
}
