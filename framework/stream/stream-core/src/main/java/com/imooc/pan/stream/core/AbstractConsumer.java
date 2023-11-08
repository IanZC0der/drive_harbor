package com.imooc.pan.stream.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import java.util.Objects;

/**
 * abstract consumer
 */
@Slf4j
public abstract class AbstractConsumer {

    /**
     * log printer
     *
     * @param message
     */
    protected void printLog(Message message) {
        log.info("{} starting consuming the message, the message is {}", this.getClass().getSimpleName(), message);
    }

    /**
     * empty message check
     *
     * @param message
     * @return
     */
    protected boolean isEmptyMessage(Message message) {
        if (Objects.isNull(message)) {
            return true;
        }
        Object payload = message.getPayload();
        if (Objects.isNull(payload)) {
            return true;
        }
        return false;
    }

}
