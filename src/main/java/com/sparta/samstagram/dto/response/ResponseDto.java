package com.sparta.samstagram.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDto<T> {
  private boolean success;
  private T data;
  private Error error;

  @Builder
  public ResponseDto(boolean success, T data, Error error) {
    this.success = success;
    this.data = data;
    this.error = error;
  }

  public ResponseDto() {}

  public static <T> ResponseDto<T> success(T data) {
    return ResponseDto.<T>builder()
            .success(true)
            .data(data)
            .error(null)
            .build();
  }

  public static <T> ResponseDto<T> fail(String code, String msg) {
    return ResponseDto.<T>builder()
            .success(false)
            .data(null)
            .error(Error.builder()
                    .code(code)
                    .msg(msg)
                    .build())
            .build();
  }

  @Getter
  static class Error {
    private String code;
    private String msg;

    @Builder
    public Error(String code, String msg) {
      this.code = code;
      this.msg = msg;
    }

    public Error() {}
  }
}
