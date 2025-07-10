package com.pig.design.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleState {
    TEMP(0,"草稿"),
    PROCESSING(1,"草稿中"),
    APPROVED(2,"已通过"),
    REJECTED(3,"已打回");

    @EnumValue
    @JsonValue
    private final int code;
    private final String desc;
}
