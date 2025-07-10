package com.pig.design.enums;

public enum StateEvent {
    APPLY,  // 从 草稿变成审批中
    AGREE,  // 从审批中变成已通过
    DISAGREE,  // 从审批中变成已打回
    EDIT  // 从已打回/已通过变成草稿
}