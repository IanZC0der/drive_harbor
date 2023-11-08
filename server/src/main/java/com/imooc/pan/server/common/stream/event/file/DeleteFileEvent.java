package com.imooc.pan.server.common.stream.event.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DeleteFileEvent implements Serializable {
    private static final long serialVersionUID = 1052162045311253928L;

    private List<Long> fileIdList;

    public DeleteFileEvent(List<Long> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
