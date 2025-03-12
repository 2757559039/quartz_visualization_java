package com.visualization.cloud.sse.sse;/**
 * @Auter 李孝在
 * @Date 2025/1/26
 */

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse
 * @className: aqsdfg
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/26 23:02
 * @version: 1.0
 */
@AllArgsConstructor
@Data
public class SubscriptionKey {
    private SubscriptionKeyEnum type; // 3层,5种
    private String actual;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionKey that = (SubscriptionKey) o;

        // 1. 比较 type 是否相等
        if (this.type != that.type) {
            return false;
        }

        // 2. 比较 actual 是否相等
        return Objects.equals(this.actual, that.actual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, actual);
    }

}