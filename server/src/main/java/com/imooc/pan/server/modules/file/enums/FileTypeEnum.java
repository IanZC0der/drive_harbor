package com.imooc.pan.server.modules.file.enums;

import com.imooc.pan.core.exception.driveHarborBusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * file type enums
 * file types, 1: normal file, 2: zipped file, 3: excel, 4: word, 5: pdf, 6: txt, 7: pics, 8: audios, 9: videos, 10: PPT, 11: code, 12: csv
 */
@AllArgsConstructor
@Getter
public enum FileTypeEnum {

    NORMAL_FILE(1, "NORMAL_FILE", 1, fileSuffix -> true),
    ARCHIVE_FILE(2, "ARCHIVE_FILE", 2, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".rar", ".zip", ".cab", ".iso", ".jar", ".ace", ".7z", ".tar", ".gz", ".arj", ".lah", ".uue", ".bz2", ".z", ".war");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    EXCEL_FILE(3, "EXCEL", 3, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".xlsx", ".xls");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    WORD_FILE(4, "WORD_FILE", 4, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".docx", ".doc");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    PDF_FILE(5, "PDF_FILE", 5, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".pdf");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    TXT_FILE(6, "TXT_FILE", 6, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".txt");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    IMAGE_FILE(7, "IMAGE_FILE", 7, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".bmp", ".gif", ".png", ".ico", ".eps", ".psd", ".tga", ".tiff", ".jpg", ".jpeg");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    AUDIO_FILE(8, "AUDIO_FILE", 8, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".mp3", ".mkv", ".mpg", ".rm", ".wma");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    VIDEO_FILE(9, "VIDEO_FILE", 9, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".avi", ".3gp", ".mp4", ".flv", ".rmvb", ".mov");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    POWER_POINT_FILE(10, "POWER_POINT_FILE", 10, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".ppt", ".pptx");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    SOURCE_CODE_FILE(11, "SOURCE_CODE_FILE", 11, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".java", ".obj", ".h", ".c", ".html", ".net", ".php", ".css", ".js", ".ftl", ".jsp", ".asp");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    }),
    CSV_FILE(12, "CSV_FILE", 12, fileSuffix -> {
        List<String> matchFileSuffixes = Arrays.asList(".csv");
        return StringUtils.isNotBlank(fileSuffix) && matchFileSuffixes.contains(fileSuffix);
    });

    /**
     * file type code
     */
    private Integer code;

    /**
     * file type desc
     */
    private String desc;

    /**
     * file type order
     * descending order
     */
    private Integer order;

    /**
     * file type matcher
     */
    private Predicate<String> tester;

    /**
     * get file type code by the suffix of the file name
     *
     * @param fileSuffix
     * @return
     */
    public static Integer getFileTypeCode(String fileSuffix) {
        Optional<FileTypeEnum> result = Arrays.stream(values())
                .sorted(Comparator.comparingInt(FileTypeEnum::getOrder).reversed())
                .filter(value -> value.getTester().test(fileSuffix))
                .findFirst();
        if (result.isPresent()) {
            return result.get().getCode();
        }
        throw new driveHarborBusinessException("Failed to get the file type");
    }

}
