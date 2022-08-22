package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseDto<?> likePost(@PathVariable Long postId, HttpServletRequest request
    ) {
        return likeService.likePost(postId, request);
    }

    @PostMapping("/{postId}/comments/{commentId}/like")
    public ResponseDto<?> likeComment(@PathVariable Long postId, @PathVariable Long commentId, HttpServletRequest request
    ) {
        return likeService.likeComment(postId, commentId, request);
    }
}
