package jungle.spaceship.member.controller.dto;

import jungle.spaceship.member.entity.alien.AlienType;
import jungle.spaceship.member.entity.family.FamilyRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FamilyInfoResponseDto {
    private List<TypeInfoDto> types;
    private List<RoleInfoDto> roles;

    public FamilyInfoResponseDto() {
        types = new ArrayList<>();
        for (AlienType alienType : AlienType.values()) {
            TypeInfoDto typeInfoDto = new TypeInfoDto(alienType, true);
            types.add(typeInfoDto);
        }

        roles = new ArrayList<>();
        for (FamilyRole familyRole : FamilyRole.values()) {
            RoleInfoDto roleInfoDto = new RoleInfoDto(familyRole, true);
            roles.add(roleInfoDto);
        }

    }

    @AllArgsConstructor
    @Getter
    public static class TypeInfoDto {
        private AlienType type;
        private boolean enabled;

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class RoleInfoDto {
        private FamilyRole role;
        private boolean enabled;
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }


}
