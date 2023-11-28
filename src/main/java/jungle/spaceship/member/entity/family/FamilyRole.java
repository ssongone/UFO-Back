package jungle.spaceship.member.entity.family;

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



    String roleName;

    FamilyRole(String roleName) {
        this.roleName = roleName;
    }
}
