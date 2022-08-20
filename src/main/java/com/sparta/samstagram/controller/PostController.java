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

    @GetMapping
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    @GetMapping("/{postId}")
    public ResponseDto<?> getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
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
