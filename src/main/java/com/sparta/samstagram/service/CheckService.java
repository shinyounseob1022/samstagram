package com.sparta.samstagram.service;

import com.sparta.samstagram.domain.Member;
import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public ResponseDto<?> checkMemberId(String memberId) {
        if (memberService.isPresentMember(memberId) != null) {
            return ResponseDto.fail("DUPLICATED_MEMBERID", "존재하는 아이디입니다.");
        }
        return ResponseDto.success(null);
    }

    public ResponseDto<?> checkNickname(String nickname) {
        if (isPresentMember(nickname) != null) {
            return ResponseDto.fail("DUPLICATED_NICKNAME", "존재하는 닉네임입니다.");
        }
        return ResponseDto.success(null);
    }

    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        System.out.println(optionalMember);
        return optionalMember.orElse(null);
    }
}
