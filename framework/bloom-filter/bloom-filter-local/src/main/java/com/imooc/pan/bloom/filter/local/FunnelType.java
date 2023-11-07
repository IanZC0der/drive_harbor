package com.imooc.pan.bloom.filter.local;

import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

/**
 * funnel type enums
 */
@AllArgsConstructor
@Getter
public enum FunnelType {

    /**
     * long funnels
     */
    LONG(Funnels.longFunnel()),
    /**
     * int funnels
     */
    INTEGER(Funnels.integerFunnel()),
    /**
     * string funnels
     */
    STRING(Funnels.stringFunnel(StandardCharsets.UTF_8));

    private Funnel funnel;

}
