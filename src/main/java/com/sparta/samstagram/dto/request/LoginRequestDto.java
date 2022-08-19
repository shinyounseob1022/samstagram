package com.sparta.samstagram.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequestDto {

  @NotBlank
  private String memberId;

  @NotBlank
  private String password;

  @Builder
  public LoginRequestDto(String memberId, String password) {
    this.memberId = memberId;
    this.password = password;
  }

  public LoginRequestDto() {}

}
