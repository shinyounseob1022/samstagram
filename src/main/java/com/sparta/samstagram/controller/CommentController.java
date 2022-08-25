package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.request.CommentRequestDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseDto<?> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto,
                                     HttpServletRequest request) {
        return commentService.createComment(postId, requestDto, request);
    }

    @GetMapping("/{postId}/comments/{nickname}")
    public ResponseDto<?> getAllComment(@PathVariable Long postId, @PathVariable String nickname) {
        return commentService.getAllComment(postId, nickname);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long postId,@PathVariable Long commentId,
                                        HttpServletRequest request) {
        return commentService.deleteComment(postId,commentId, request);
    }
}
