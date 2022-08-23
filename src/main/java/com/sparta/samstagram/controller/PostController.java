package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.request.PostRequestDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseDto<?> createPost(@RequestPart("data") PostRequestDto requestDto,
                                     @RequestPart("img") MultipartFile multipartFile,
                                     HttpServletRequest request) throws IOException {
        return postService.createPost(requestDto, multipartFile, request);
    }

    @GetMapping("/all/{nickname}")
    public ResponseDto<?> getAllPosts(@PathVariable String nickname) {
        return postService.getAllPost(nickname);
    }

    @GetMapping("/{postId}/{nickname}")
    public ResponseDto<?> getPost(@PathVariable Long postId, @PathVariable String nickname) {
        return postService.getPost(postId, nickname);
    }

    @PutMapping("/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId,
                                     @RequestPart("data") PostRequestDto requestDto,
                                     @RequestPart("img") MultipartFile multipartFile,
                                     HttpServletRequest request) throws IOException {
        return postService.updatePost(postId, requestDto, multipartFile, request);
    }

    @DeleteMapping("/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        return postService.deletePost(postId, request);
    }
}
