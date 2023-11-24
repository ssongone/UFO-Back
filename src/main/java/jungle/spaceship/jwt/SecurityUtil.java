package jungle.spaceship.jwt;

import jungle.spaceship.member.entity.Member;
import jungle.spaceship.member.entity.MemberDetail;
import jungle.spaceship.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class SecurityUtil {

    private final MemberRepository memberRepository;

    public Member extractMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        Long memberId = Long.valueOf(user.getUsername());
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoSuchElementException("해당하는 사용자가 없습니다"));
    }

    public Long extractFamilyId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetail user = (MemberDetail) authentication.getPrincipal();
        return Long.valueOf(user.getFamilyId());
    }


}
