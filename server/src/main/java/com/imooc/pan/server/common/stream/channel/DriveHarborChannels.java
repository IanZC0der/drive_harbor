package com.imooc.pan.server.common.stream.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * event channels
 */
public interface DriveHarborChannels {

    String TEST_INPUT = "testInput";
    String TEST_OUTPUT = "testOutput";

    String ERROR_LOG_INPUT = "errorLogInput";
    String ERROR_LOG_OUTPUT = "errorLogOutput";

    String DELETE_FILE_INPUT = "deleteFileInput";
    String DELETE_FILE_OUTPUT = "deleteFileOutput";

    String FILE_RESTORE_INPUT = "fileRestoreInput";
    String FILE_RESTORE_OUTPUT = "fileRestoreOutput";

    String PHYSICAL_DELETE_FILE_INPUT = "physicalDeleteFileInput";
    String PHYSICAL_DELETE_FILE_OUTPUT = "physicalDeleteFileOutput";

    String USER_SEARCH_INPUT = "userSearchInput";
    String USER_SEARCH_OUTPUT = "userSearchOutput";

    /**
     * test input channel
     *
     * @return
     */
    @Input(TEST_INPUT)
    SubscribableChannel testInput();

    /**
     * test output channel
     *
     * @return
     */
    @Output(TEST_OUTPUT)
    MessageChannel testOutput();

    @Input(DriveHarborChannels.ERROR_LOG_INPUT)
    SubscribableChannel errorLogInput();

    @Output(DriveHarborChannels.ERROR_LOG_OUTPUT)
    MessageChannel errorLogOutput();

    @Input(DriveHarborChannels.DELETE_FILE_INPUT)
    SubscribableChannel deleteFileInput();

    @Output(DriveHarborChannels.DELETE_FILE_OUTPUT)
    MessageChannel deleteFileOutput();

    @Input(DriveHarborChannels.FILE_RESTORE_INPUT)
    SubscribableChannel fileRestoreInput();

    @Output(DriveHarborChannels.FILE_RESTORE_OUTPUT)
    MessageChannel fileRestoreOutput();

    @Input(DriveHarborChannels.PHYSICAL_DELETE_FILE_INPUT)
    SubscribableChannel physicalDeleteFileInput();

    @Output(DriveHarborChannels.PHYSICAL_DELETE_FILE_OUTPUT)
    MessageChannel physicalDeleteFileOutput();

    @Input(DriveHarborChannels.USER_SEARCH_INPUT)
    SubscribableChannel userSearchInput();

    @Output(DriveHarborChannels.USER_SEARCH_OUTPUT)
    MessageChannel userSearchOutput();

}
