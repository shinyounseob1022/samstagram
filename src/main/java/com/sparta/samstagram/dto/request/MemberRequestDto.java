package com.sparta.samstagram.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class MemberRequestDto {

  @NotBlank
  @Size(min = 6, max = 12)
  @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9]{5,11}$\n") // 시작은 영문으로만, 특수문자 안되며 영문, 숫자로만 이루어진 6 ~ 12자
  private String memberId;

  @NotBlank
  @Size(min = 2, max = 12)
  @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,12}$") // 2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성
  private String nickname;

  @NotBlank
  @Size(min = 6)
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$") // 영문자, 숫자, 특수문자 최소 하나씩 포함, 6자 이상
  private String password;

  public MemberRequestDto(String memberId, String nickname, String password) {
    this.memberId = memberId;
    this.nickname = nickname;
    this.password = password;
  }

  public MemberRequestDto() {}

}
