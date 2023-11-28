package jungle.spaceship.config;

import jungle.spaceship.member.entity.family.FamilyRole;
import jungle.spaceship.photo.repository.FamilyRoleInfoRepository;
import jungle.spaceship.photo.entity.FamilyRoleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBInitializer {

    private final FamilyRoleInfoRepository familyRoleInfoRepository;

    /**
     * 초기화를 수행하기 위해 (스프링 컨테이너가) 의존성 주입(을 강제로 완료 시킨 후) 실행해야 하는 메서드에 사용
     * - 스프링 컨테이너가 주체적으로 개발자 대신 실행시킴
     *
     */

    @PostConstruct
    private void init(){
        initPhotoTag();

    }
    public void initPhotoTag(){
        try {
            Set<FamilyRole> familyRoles = familyRoleInfoRepository.findAll()
                    .stream()
                    .map(FamilyRoleInfo::getFamilyRole)
                    .collect(Collectors.toSet());

            for(FamilyRole role : FamilyRole.values()){
                if(!familyRoles.contains(role)){
                    familyRoleInfoRepository.save(new FamilyRoleInfo(role));
                    log.info("New Family Role, {} 이 추가되었습니다.", role);
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage(), e);

        }

    }

}
