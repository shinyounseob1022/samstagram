package com.sparta.samstagram.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
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
    @JsonProperty(value = "isLike")
    private boolean isLike;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private  LocalDate modifiedAt;

    @Builder
    public PostResponseDto(Long postId, String author, String authorImgUrl, String content, String imgUrl,
                           Long postLikeCnt, Long commentCnt, boolean isEditMode, boolean isModalMode, boolean isLike,
                           LocalDate createdAt,  LocalDate modifiedAt) {
        this.postId = postId;
        this.author = author;
        this.authorImgUrl = authorImgUrl;
        this.content = content;
        this.imgUrl = imgUrl;
        this.postLikeCnt = postLikeCnt;
        this.commentCnt = commentCnt;
        this.isEditMode = isEditMode;
        this.isModalMode = isModalMode;
        this.isLike = isLike;
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

    @JsonIgnore
    public boolean isLike() {
        return isLike;
    }
}
