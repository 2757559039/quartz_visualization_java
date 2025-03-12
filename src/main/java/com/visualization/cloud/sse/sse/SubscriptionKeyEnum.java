package com.visualization.cloud.sse.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Auter 李孝在
 * @Date 2025/1/26
 */
@AllArgsConstructor
@Getter
public enum SubscriptionKeyEnum {
    //全局等级
    GLOBAL("global"),
    //组级
    GROUP_JOB("group_job"),
    GROUP_TRIGGER("group_trigger"),
    //键级
    KEY_JOB("key_job"),
    KEY_TRIGGER("key_trigger");
    private final String type;
}
