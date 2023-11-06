package com.imooc.pan.server.common.event.test;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * test event async
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TestEvent extends ApplicationEvent {

    private String message;

    public TestEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

}
