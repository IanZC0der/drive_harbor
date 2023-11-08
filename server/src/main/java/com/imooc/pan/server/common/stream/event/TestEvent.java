package com.imooc.pan.server.common.stream.event;

import lombok.Data;

import java.io.Serializable;

/**
 * test event
 */
@Data
public class TestEvent implements Serializable {

    private static final long serialVersionUID = -67041500843171728L;

    /**
     * message properties-name
     */
    private String name;
}
