package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.request.CommentRequestDto;
import com.sparta.samstagram.dto.request.PostRequestDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseDto<?> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                     HttpServletRequest request) {
        return commentService.createComment(id, requestDto, request);
    }

    @GetMapping("/{postId}/comments")
    public ResponseDto<?> getAllComment(@PathVariable Long id) {
        return commentService.getAllComment(id);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long postId,@PathVariable Long commentId,
                                        @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(postId,commentId ,requestDto, request);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long postId,@PathVariable Long commentId,
                                        HttpServletRequest request) {
        return commentService.deleteComment(postId,commentId, request);
    }
}
