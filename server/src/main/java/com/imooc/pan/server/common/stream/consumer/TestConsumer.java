package com.imooc.pan.server.common.stream.consumer;

import com.imooc.pan.server.common.stream.channel.DriveHarborChannels;
import com.imooc.pan.server.common.stream.event.TestEvent;
import com.imooc.pan.stream.core.AbstractConsumer;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * test message consumer
 */
@Component
public class TestConsumer extends AbstractConsumer {

    /**
     * consumer test message
     *
     * @param message
     */
    @StreamListener(DriveHarborChannels.TEST_INPUT)
    public void consumeTestMessage(Message<TestEvent> message) {
        printLog(message);
    }

}
