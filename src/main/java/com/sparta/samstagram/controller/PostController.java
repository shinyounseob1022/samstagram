package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.request.PostRequestDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
                                     @RequestPart("image") MultipartFile multipartFile,
                                     HttpServletRequest request) throws IOException {
        return postService.createPost(requestDto, multipartFile, request);
    }
}
