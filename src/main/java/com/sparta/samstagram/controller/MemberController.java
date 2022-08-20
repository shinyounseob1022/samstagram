package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.request.LoginRequestDto;
import com.sparta.samstagram.dto.request.MemberRequestDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/signup")
  public ResponseDto<?> signup(@RequestPart("data") MemberRequestDto requestDto,
                               @RequestPart("img") MultipartFile multipartFile) throws IOException {
    return memberService.createMember(requestDto, multipartFile);
  }

  @PostMapping("/login")
  public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
      HttpServletResponse response) {
    return memberService.login(requestDto, response);
  }

//  @RequestMapping(value = "/api/auth/member/reissue", method = RequestMethod.POST)
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    return memberService.reissue(request, response);
//  }
//
//  @RequestMapping(value = "/api/auth/member/logout", method = RequestMethod.POST)
//  public ResponseDto<?> logout(HttpServletRequest request) {
//    return memberService.logout(request);
//  }
}
