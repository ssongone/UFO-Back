package jungle.spaceship.member.entity.family;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum FamilyRole {
    DAD("아빠"),
    MOM("엄마"),

    FIRST("첫째"),
    SECOND("둘째"),
    THIRD("셋째"),
    FOURTH("넷째"),
    FIFTH("다섯째"),
    SIXTH("여섯째"),

    GRANDFATHER("할아버지"),
    GRANDMOTHER("할머니"),

    UNCLE("삼촌"),

    EXTRA("기타");

    private final String roleName;

    FamilyRole(String roleName) {
        this.roleName = roleName;
    }

    private static final Map<String, FamilyRole> FamilyRole_MAP =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(FamilyRole::getRoleName, Function.identity())));

    public static FamilyRole find(String roleName) {
        if (FamilyRole_MAP.containsKey(roleName)) {
            return FamilyRole_MAP.get(roleName);
        }
        throw new IllegalArgumentException("가족 역할을 찾을 수 없습니다.");
    }
}
