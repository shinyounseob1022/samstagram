package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private String author;
    private String authorImgUrl;
    private String title;
    private String content;
    private String imgUrl;
    private Long postLikeCnt;
    private Long commentCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public PostResponseDto(String author, String authorImgUrl, String title,
                           String content, String imgUrl, Long postLikeCnt, Long commentCnt,
                           LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.author = author;
        this.authorImgUrl = authorImgUrl;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.postLikeCnt = postLikeCnt;
        this.commentCnt = commentCnt;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PostResponseDto() {}
}
