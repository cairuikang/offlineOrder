package com.jhlc.offlineorder.conmmon.constant;

import lombok.Getter;

/**
 * @author 10096
 */

@Getter
public enum UserType {
    USER_TYPE1("渠道管理员", 1),
    USER_TYPE2("普通会员", 2),
    USER_TYPE3("渠道超管", 3),
    USER_TYPE4("", 4),
    USER_TYPE5("组织管理员", 5),
    USER_TYPE6("组织会员", 6),
    USER_TYPE7("年安管理人员", 7);

    private String name;
    private int index;

    UserType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getNameByIndex(int index) {
        for (UserType c : UserType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return "";
    }
    public static UserType getByIndex(int index) {
        for (UserType c : UserType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }

}
