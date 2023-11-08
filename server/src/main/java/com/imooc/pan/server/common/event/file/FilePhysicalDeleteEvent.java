//package com.imooc.pan.server.common.event.file;
//
//import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.springframework.context.ApplicationEvent;
//
//import java.util.List;
//
///**
// * File Physical Delete Event
// */
//@Getter
//@Setter
//@EqualsAndHashCode
//@ToString
//public class FilePhysicalDeleteEvent extends ApplicationEvent {
//
//    /**
//     * all records of the deleted files
//     */
//    private List<driveHarborUserFile> allRecords;
//
//    public FilePhysicalDeleteEvent(Object source, List<driveHarborUserFile> allRecords) {
//        super(source);
//        this.allRecords = allRecords;
//    }
//
//}
