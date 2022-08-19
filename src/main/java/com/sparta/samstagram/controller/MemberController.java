package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.request.LoginRequestDto;
import com.sparta.samstagram.dto.request.MemberRequestDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/signup")
  public ResponseDto<?> signup(@RequestBody MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
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
