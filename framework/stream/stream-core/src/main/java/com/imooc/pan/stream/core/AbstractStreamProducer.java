package com.imooc.pan.stream.core;

import com.google.common.collect.Maps;
import com.imooc.pan.core.exception.driveHarborFrameworkException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;
import java.util.Objects;

/**
 * abstract stream producer
 */
public abstract class AbstractStreamProducer implements IStreamProducer {

    @Autowired
    private Map<String, MessageChannel> channelMap;

    /**
     * send message
     *
     * @param channelName
     * @param deploy
     * @return
     */
    @Override
    public boolean sendMessage(String channelName, Object deploy) {
        return sendMessage(channelName, deploy, Maps.newHashMap());
    }

    /**
     * send message
     * 1. verify parameters
     * 2. presend
     * 3. dosend
     * 4. aftersend
     * 4. return
     *
     * @param channelName
     * @param deploy
     * @param headers
     * @return
     */
    @Override
    public boolean sendMessage(String channelName, Object deploy, Map<String, Object> headers) {
        if (StringUtils.isBlank(channelName) || Objects.isNull(deploy)) {
            throw new driveHarborFrameworkException("the channelName or deploy can not be empty!");
        }
        if (MapUtils.isEmpty(channelMap)) {
            throw new driveHarborFrameworkException("the channelMap can not be empty!");
        }
        MessageChannel channel = channelMap.get(channelName);
        if (Objects.isNull(channel)) {
            throw new driveHarborFrameworkException("the channel named" + channelName + " can not be found!");
        }
        Message message = MessageBuilder.createMessage(deploy, new MessageHeaders(headers));
        preSend(message);
        boolean result = channel.send(message);
        afterSend(message, result);
        return result;
    }

    /**
     * aftersend
     * implemented by child class
     *
     * @param message
     * @param result
     */
    protected abstract void afterSend(Message message, boolean result);

    /**
     * presend
     * implemented by child class
     *
     * @param message
     */
    protected abstract void preSend(Message message);

}
