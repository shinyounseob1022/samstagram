package com.sparta.samstagram.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {
  private String nickname;

  @Builder
  public MemberResponseDto(String nickname) {
    this.nickname = nickname;
  }

  public MemberResponseDto() {}
}
