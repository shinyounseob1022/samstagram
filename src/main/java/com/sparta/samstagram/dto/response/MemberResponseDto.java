package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {
  private String nickname;

  @Builder
  public MemberResponseDto(String nickname) {
    this.nickname = nickname;
  }

  public MemberResponseDto() {}
}
