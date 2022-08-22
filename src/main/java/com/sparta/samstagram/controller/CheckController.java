package com.sparta.samstagram.controller;

import com.sparta.samstagram.dto.response.ResponseDto;
import com.sparta.samstagram.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check/members")
@RequiredArgsConstructor
public class CheckController {
    private final CheckService checkService;

    @PostMapping("/memberId")
    public ResponseDto<?> checkMemberId(@RequestParam String memberId) {
        System.out.println(memberId);
        return checkService.checkMemberId(memberId);
    }

    @PostMapping("/nickname")
    public ResponseDto<?> checkNickname(@RequestParam String nickname) {
        System.out.println(nickname);
        return checkService.checkNickname(nickname);
    }
}
