package com.example.mixmix.member.application;

import com.example.mixmix.member.api.dto.request.JoinMyPageUpdateReqDto;
import com.example.mixmix.member.api.dto.response.JoinMyPageInfoResDto;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.ExistsNicknameException;
import com.example.mixmix.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public JoinMyPageInfoResDto joinRequest(String email, JoinMyPageUpdateReqDto joinMyPageUpdateReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        if (isNicknameDuplicate(joinMyPageUpdateReqDto.nickname())) {
            throw new ExistsNicknameException();
        }

        member.update(email,
                joinMyPageUpdateReqDto.introduction(),
                joinMyPageUpdateReqDto.nationality(),
                joinMyPageUpdateReqDto.school(),
                joinMyPageUpdateReqDto.nickname());

        return JoinMyPageInfoResDto.from(member);
    }

    private boolean isNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(normalizeNickname(nickname));
    }

    private String normalizeNickname(String nickname) {
        return nickname.replaceAll("\\s+", "");
    }
}