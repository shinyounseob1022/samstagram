package com.sparta.samstagram.service;

import com.sparta.samstagram.domain.Member;
import com.sparta.samstagram.dto.request.LoginRequestDto;
import com.sparta.samstagram.dto.request.MemberRequestDto;
import com.sparta.samstagram.dto.request.TokenDto;
import com.sparta.samstagram.dto.response.MemberResponseDto;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.jwt.TokenProvider;
import com.sparta.samstagram.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;
  private final S3UploadService s3UploadService;

  @Transactional
  public ResponseDto<?> createMember(MemberRequestDto requestDto,
                                     MultipartFile multipartFile) throws IOException {

    if (null != isPresentMember(requestDto.getMemberId())) {
      return ResponseDto.fail("DUPLICATED_MEMBERID",
          "이미 존재하는 아이디입니다.");
    }

    String memberImgUrl = null;
    if (!multipartFile.isEmpty()) {
      memberImgUrl = s3UploadService.upload(multipartFile, "samstagram/authorImg");
      System.out.println("authorImgUrl: "+memberImgUrl);
    }

    Member member = Member.builder()
            .memberId(requestDto.getMemberId())
            .nickname(requestDto.getNickname())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .memberImgUrl(memberImgUrl)
            .build();
    memberRepository.save(member);
    return ResponseDto.success(
        MemberResponseDto.builder()
            .nickname(member.getNickname())
            .build()
    );
  }

  @Transactional
  public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
    Member member = isPresentMember(requestDto.getMemberId());
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "존재하지 않는 아이디입니다.");
    }

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(requestDto.getMemberId(), requestDto.getPassword());
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
    tokenToHeaders(tokenDto, response);
    System.out.println(tokenDto.getAccessToken());

    return ResponseDto.success(
        MemberResponseDto.builder()
            .nickname(member.getNickname())
            .build()
    );
  }

//  @Transactional
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
//    }
//    Member member = tokenProvider.getMemberFromAuthentication();
//    if (null == member) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "member not found");
//    }
//
//    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Authorization"));
//    RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);
//
//    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
//    }
//
//    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//    refreshToken.updateValue(tokenDto.getRefreshToken());
//    tokenToHeaders(tokenDto, response);
//    return ResponseDto.success("success");
//  }
//
//  public ResponseDto<?> logout(HttpServletRequest request) {
//    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
//    }
//    Member member = tokenProvider.getMemberFromAuthentication();
//    if (null == member) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "member not found");
//    }
//
//    return tokenProvider.deleteRefreshToken(member);
//  }

  @Transactional(readOnly = true)
  public Member isPresentMember(String memberId) {
    Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
    return optionalMember.orElse(null);
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
//    response.addHeader("Refresh-Token", "Bearer " + tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

}
