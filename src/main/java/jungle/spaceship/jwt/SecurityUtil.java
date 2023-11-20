package jungle.spaceship.jwt;

import jungle.spaceship.entity.Member;
import jungle.spaceship.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class SecurityUtil {

    private final MemberRepository memberRepository;

    public  Member extractMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long memberId = Long.valueOf(user.getUsername());

        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new NoSuchElementException("해당하는 사용자가 없습니다"));
    }
}
