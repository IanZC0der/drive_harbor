package com.imooc.pan.server.common.stream.event.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

/**
 * file restore event
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class FileRestoreEvent implements Serializable {
    private static final long serialVersionUID = -5823524176954356392L;

    /**
     * list of ids of files successfully recoverred
     */
    private List<Long> fileIdList;

    public FileRestoreEvent(List<Long> fileIdList) {
        this.fileIdList = fileIdList;
    }

}
