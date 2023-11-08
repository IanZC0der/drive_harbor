package com.imooc.pan.server.common.stream.event.file;

import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

/**
 * File Physical Delete Event
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FilePhysicalDeleteEvent implements Serializable {
    private static final long serialVersionUID = 3959988542308316628L;
    /**
     * all records of the deleted files
     */
    private List<driveHarborUserFile> allRecords;

    public FilePhysicalDeleteEvent(List<driveHarborUserFile> allRecords) {
        this.allRecords = allRecords;
    }

}
