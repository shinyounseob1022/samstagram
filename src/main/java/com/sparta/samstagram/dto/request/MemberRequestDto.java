package com.sparta.samstagram.dto.request;

import lombok.Getter;

@Getter
public class MemberRequestDto {

//  @NotBlank
//  @Size(min = 6, max = 12)
//  @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
  private String memberId;

//  @NotBlank
//  @Size(min = 1, max = 12)
//  @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
  private String nickname;

//  @NotBlank
//  @Size(min = 6, max = 20)
//  @Pattern(regexp = "[a-z\\d]*${3,20}")
  private String password;

  public MemberRequestDto(String memberId, String nickname, String password) {
    this.memberId = memberId;
    this.nickname = nickname;
    this.password = password;
  }

  public MemberRequestDto() {}

}
