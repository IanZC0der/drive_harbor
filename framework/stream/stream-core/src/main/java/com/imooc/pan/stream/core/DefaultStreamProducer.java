package com.imooc.pan.stream.core;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * default stream producer
 * use default ones
 */
@Component(value = "defaultStreamProducer")
public class DefaultStreamProducer extends AbstractStreamProducer{
    /**
     * aftersend
     * implemented by child class
     *
     * @param message
     * @param result
     */
    @Override
    protected void afterSend(Message message, boolean result) {

    }

    /**
     * presend
     * implemented by child class
     *
     * @param message
     */
    @Override
    protected void preSend(Message message) {

    }
}
