package com.imooc.pan.server.common.stream.event.log;

import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ErrorLogEvent implements Serializable {
    private static final long serialVersionUID = -8680220649817382860L;
    /**
     * error message
     */
    private String errorMsg;

    /**
     * user id
     */
    private Long userId;

    public ErrorLogEvent(String errorMsg, Long userId) {
        this.errorMsg = errorMsg;
        this.userId = userId;
    }
}
