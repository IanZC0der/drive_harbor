package com.imooc.pan.stream.core;

import java.util.Map;

/**
 * stream producer interface
 */
public interface IStreamProducer {

    /**
     * send message
     *
     * @param channelName
     * @param deploy
     * @return
     */
    boolean sendMessage(String channelName, Object deploy);

    /**
     * send message
     *
     * @param channelName
     * @param deploy
     * @param headers
     * @return
     */
    boolean sendMessage(String channelName, Object deploy, Map<String, Object> headers);

}
