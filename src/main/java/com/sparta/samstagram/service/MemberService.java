package com.sparta.samstagram.service;

import com.sparta.samstagram.domain.Member;
import com.sparta.samstagram.domain.Post;
import com.sparta.samstagram.domain.PostLike;
import com.sparta.samstagram.dto.request.LoginRequestDto;
import com.sparta.samstagram.dto.request.MemberRequestDto;
import com.sparta.samstagram.dto.request.TokenDto;
import com.sparta.samstagram.dto.response.*;
import com.sparta.samstagram.jwt.TokenProvider;
import com.sparta.samstagram.repository.CommentRepository;
import com.sparta.samstagram.repository.MemberRepository;
import com.sparta.samstagram.repository.PostLikeRepository;
import com.sparta.samstagram.repository.PostRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;
  private final S3UploadService s3UploadService;
  private final PostLikeRepository postLikeRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public ResponseDto<?> createMember(MemberRequestDto requestDto,
                                     MultipartFile multipartFile) throws IOException {

    if (null != isPresentMemberByMemberId(requestDto.getMemberId())) {
      return ResponseDto.fail("DUPLICATED_MEMBERID",
          "이미 존재하는 아이디입니다.");
    }

    if (null != isPresentMemberByNickname(requestDto.getNickname())) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
              "이미 존재하는 닉네임입니다.");
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
    Member member = isPresentMemberByMemberId(requestDto.getMemberId());
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

  @Transactional
  public ResponseDto<?> getMypage(String nickname) {
    Member member = isPresentMemberByNickname(nickname);

    if (null == member) {
      return ResponseDto.fail("NICKNAME_NOT_FOUND",
              "존재하지 않는 닉네임입니다.");
    }

    return ResponseDto.success(
            MypageResponseDto.builder()
                    .nickname(member.getNickname())
                    .memberImgUrl(member.getMemberImgUrl())
                    .build()
    );
  }

  @Transactional
  public ResponseDto<?> editMypage(String nickname, MultipartFile multipartFile) throws IOException {
    Member member = isPresentMemberByNickname(nickname);

    if (member == null) {
      return ResponseDto.fail("NICKNAME_NOT_FOUND",
              "존재하지 않는 닉네임입니다.");
    }

    String memberImgUrl = member.getMemberImgUrl();
    if (!multipartFile.isEmpty()) {
      memberImgUrl = s3UploadService.upload(multipartFile, "samstagram/authorImg");
    } else {
      memberImgUrl = null;
    }

    member.update(memberImgUrl);

    return ResponseDto.success(
            MypageResponseDto.builder()
                    .nickname(member.getNickname())
                    .memberImgUrl(memberImgUrl)
                    .build()
    );
  }

  @Transactional
  public ResponseDto<?> getProfile(String nickname) {
    Member member = isPresentMemberByNickname(nickname);

    if (member == null) {
      return ResponseDto.fail("NICKNAME_NOT_FOUND",
              "존재하지 않는 닉네임입니다.");
    }

    List<Post> myPostList = member.getPostList();
    List<PostResponseDto> myPostResponseDtoList = new ArrayList<>();

    for (Post myPost : myPostList) {
      myPostResponseDtoList.add(
              PostResponseDto.builder()
                      .postId(myPost.getId())
                      .author(myPost.getMember().getNickname())
                      .imgUrl(myPost.getImgUrl())
                      .postLikeCnt(postLikeRepository.countByPost(myPost))
                      .commentCnt(commentRepository.countByPost(myPost))
                      .isModalMode(false)
                      .createdAt(myPost.getCreatedAt())
                      .modifiedAt(myPost.getModifiedAt())
                      .build()
      );
    }

    List<PostLike> myLikePostList = member.getPostLikeList();
    List<PostResponseDto> myLikePostResponseDtoList = new ArrayList<>();

    for (PostLike myPostLike : myLikePostList) {
      myLikePostResponseDtoList.add(
              PostResponseDto.builder()
                      .postId(myPostLike.getPost().getId())
                      .author(myPostLike.getMember().getNickname())
                      .imgUrl(myPostLike.getPost().getImgUrl())
                      .postLikeCnt(postLikeRepository.countByPost(myPostLike.getPost()))
                      .commentCnt(commentRepository.countByPost(myPostLike.getPost()))
                      .isModalMode(false)
                      .createdAt(myPostLike.getPost().getCreatedAt())
                      .modifiedAt(myPostLike.getPost().getModifiedAt())
                      .build()
      );
    }

    return ResponseDto.success(
            ProfileResponseDto.builder()
                    .myPost(myPostResponseDtoList)
                    .myLikePost(myLikePostResponseDtoList)
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
  public Member isPresentMemberByMemberId(String memberId) {
    Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
    System.out.println(optionalMember);
    return optionalMember.orElse(null);
  }

  @Transactional(readOnly = true)
  public Member isPresentMemberByNickname(String nickname) {
    Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
    System.out.println(optionalMember);
    return optionalMember.orElse(null);
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
//    response.addHeader("Refresh-Token", "Bearer " + tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

}
