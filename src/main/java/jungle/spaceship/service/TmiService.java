package jungle.spaceship.service;

import jungle.spaceship.controller.dto.TmiDto;
import jungle.spaceship.entity.Member;
import jungle.spaceship.entity.Tmi;
import jungle.spaceship.jwt.SecurityUtil;
import jungle.spaceship.repository.TmiRepository;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TmiService {

    private final SecurityUtil securityUtil;
    private final TmiRepository tmiRepository;


    public ExtendedResponse<TmiDto> registerTmi(TmiDto tmiDto) {
        Member member = securityUtil.extractMember();
        Tmi tmi = tmiDto.getTmi(member);
        tmiRepository.save(tmi);
        return new ExtendedResponse<>(tmiDto, HttpStatus.CREATED.value(), "Tmi 등록 완료");
    }
}
