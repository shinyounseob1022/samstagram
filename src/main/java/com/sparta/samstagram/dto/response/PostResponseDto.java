package com.sparta.samstagram.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "isEditMode")
    private boolean isEditMode;
    @JsonProperty(value = "isModalMode")
    private boolean isModalMode;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public PostResponseDto(Long postId, String author, String authorImgUrl, String content, String imgUrl,
                           Long postLikeCnt, Long commentCnt, boolean isEditMode, boolean isModalMode,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.postId = postId;
        this.author = author;
        this.authorImgUrl = authorImgUrl;
        this.content = content;
        this.imgUrl = imgUrl;
        this.postLikeCnt = postLikeCnt;
        this.commentCnt = commentCnt;
        this.isEditMode = isEditMode;
        this.isModalMode = isModalMode;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PostResponseDto() {}

    @JsonIgnore
    public boolean isEditMode() {
        return isEditMode;
    }

    @JsonIgnore
    public boolean isModalMode() {
        return isModalMode;
    }
}
