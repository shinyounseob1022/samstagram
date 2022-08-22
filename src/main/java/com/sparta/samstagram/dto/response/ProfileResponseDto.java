package com.sparta.samstagram.dto.response;

import com.sparta.samstagram.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProfileResponseDto<T> {
    private T myPost;
    private T myLikePost;

    @Builder
    public ProfileResponseDto(T myPost, T myLikePost) {
        this.myPost = myPost;
        this.myLikePost = myLikePost;
    }

    public ProfileResponseDto() {}
}
