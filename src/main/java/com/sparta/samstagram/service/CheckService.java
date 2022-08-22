package com.sparta.samstagram.service;

import com.sparta.samstagram.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckService {
    private final MemberService memberService;

    public ResponseDto<?> checkMemberId(String memberId) {
        if (memberService.isPresentMemberByMemberId(memberId) != null) {
            return ResponseDto.fail("DUPLICATED_MEMBERID", "존재하는 아이디입니다.");
        }
        return ResponseDto.success(null);
    }

    public ResponseDto<?> checkNickname(String nickname) {
        if (memberService.isPresentMemberByNickname(nickname) != null) {
            return ResponseDto.fail("DUPLICATED_NICKNAME", "존재하는 닉네임입니다.");
        }
        return ResponseDto.success(null);
    }

}
