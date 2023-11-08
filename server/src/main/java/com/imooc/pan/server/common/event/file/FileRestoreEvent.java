//package com.imooc.pan.server.common.event.file;
//
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.springframework.context.ApplicationEvent;
//
//import java.util.List;
//
///**
// * file restore event
// */
//@EqualsAndHashCode
//@ToString
//@Getter
//@Setter
//public class FileRestoreEvent extends ApplicationEvent {
//
//    /**
//     * list of ids of files successfully recoverred
//     */
//    private List<Long> fileIdList;
//
//    public FileRestoreEvent(Object source, List<Long> fileIdList) {
//        super(source);
//        this.fileIdList = fileIdList;
//    }
//
//}
