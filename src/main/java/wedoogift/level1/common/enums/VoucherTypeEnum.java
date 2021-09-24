package wedoogift.level1.common.enums;

import lombok.Getter;

import java.util.Arrays;

public enum VoucherTypeEnum {
    FOOD("FOOD"),
    GIFT("GIFT");

    @Getter
    private final String code;

    VoucherTypeEnum(String code) {
        this.code = code;
    }

    public static VoucherTypeEnum getFromCode(String code) {
        return Arrays.stream(VoucherTypeEnum.values())
                .filter(v -> v.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
