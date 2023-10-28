package com.imooc.pan.core.utils;

import cn.hutool.core.date.DateUtil;
import com.imooc.pan.core.constants.driveHarborConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.createFile;

/**
 * file utils
 */
public class FileUtil {
    /**
     * get file suffix
     *
     * @param filename
     * @return
     */
    public static String getFileSuffix(String filename) {
        if (StringUtils.isBlank(filename) || filename.lastIndexOf(driveHarborConstants.POINT_STR) == driveHarborConstants.MINUS_ONE_INT) {
            return driveHarborConstants.EMPTY_STR;
        }
        return filename.substring(filename.lastIndexOf(driveHarborConstants.POINT_STR)).toLowerCase();
    }

    public static String byteCountToDisplaySize(Long totalSize) {
        if(Objects.isNull(totalSize)){
            return driveHarborConstants.EMPTY_STR;
        }
        return org.apache.commons.io.FileUtils.byteCountToDisplaySize(totalSize);
    }

    /**
     * delete the file based on the path list
     * @param realFilePathList
     */
    public static void deleteFiles(List<String> realFilePathList) throws IOException {
        if (CollectionUtils.isEmpty(realFilePathList)) {
            return;
        }
        for (String realFilePath : realFilePathList) {
            org.apache.commons.io.FileUtils.forceDelete(new File(realFilePath));
        }
    }

    /**
     * generate the path of the file saved
     * year + month + day + random file name
     * @param basePath
     * @param filename
     * @return
     */
    public static String generateStoreFileRealPath(String basePath, String filename) {
        return new StringBuffer(basePath)
                .append(File.separator)
                .append(DateUtil.thisYear())
                .append(File.separator)
                .append(DateUtil.thisMonth() + 1)
                .append(File.separator)
                .append(DateUtil.thisDayOfMonth())
                .append(File.separator)
                .append(UUIDUtil.getUUID())
                .append(getFileSuffix(filename))
                .toString();

    }

    /**
     * generate the default save path
     * curret user's home path + driveharbor
     * @return
     */
    public static String generateDefaultStoreFileRealPath() {
        return new StringBuffer(System.getProperty("user.home"))
                .append(File.separator)
                .append("driveharbor")
                .toString();
    }

    /**
     * write the file input stream to the file
     * applied the sendfile() in os
     * @param inputStream
     * @param targetFile
     * @param totalSize
     */
    public static void writeStream2File(InputStream inputStream, File targetFile, Long totalSize) throws IOException{
        createFile(targetFile);
        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "rw");
        FileChannel outputChannel = randomAccessFile.getChannel();
        ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
        outputChannel.transferFrom(inputChannel, 0L, totalSize);
        inputChannel.close();
        outputChannel.close();
        randomAccessFile.close();
        inputStream.close();
    }

    /**
     * create file
     * @param targetFile
     * @throws IOException
     */
    public static void createFile(File targetFile) throws IOException{
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        targetFile.createNewFile();
    }

    /**
     * generateDefaultStoreFileChunkRealPath
     * @return
     */
    public static String generateDefaultStoreFileChunkRealPath() {
        return new StringBuffer(System.getProperty("user.home"))
                .append(File.separator)
                .append("driveharbor")
                .append(File.separator)
                .append("chunks")
                .toString();
    }

    /**
     * generate the chunk real path
     * basepath + year +month + day + identifier + random name + __,__ + chunk number
     * @param basePath
     * @param identifier
     * @param chunkNumber
     * @return
     */
    public static String generateStoreFileChunkRealPath(String basePath, String identifier, Integer chunkNumber) {
        return new StringBuffer(basePath)
                .append(File.separator)
                .append(DateUtil.thisYear())
                .append(File.separator)
                .append(DateUtil.thisMonth() + 1)
                .append(File.separator)
                .append(DateUtil.thisDayOfMonth())
                .append(File.separator)
                .append(identifier)
                .append(File.separator)
                .append(UUIDUtil.getUUID())
                .append(driveHarborConstants.COMMON_SEPARATOR)
                .append(chunkNumber)
                .toString();
    }

    /**
     * write the merged chunks
     * @param target
     * @param source
     */
    public static void appendWrite(Path target, Path source) throws IOException{
        Files.write(target, Files.readAllBytes(source), StandardOpenOption.APPEND);
    }

    /**
     * apply the transfer-to method to write file to the output stream
     * @param fileInputStream
     * @param outputStream
     * @param length
     * @throws IOException
     */
    public static void writeFile2OutputStream(FileInputStream fileInputStream, OutputStream outputStream, long length) throws IOException{
        FileChannel fileChannel = fileInputStream.getChannel();
        WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);
        fileChannel.transferTo(driveHarborConstants.ZERO_LONG, length, writableByteChannel);
        outputStream.flush();
        fileInputStream.close();
        outputStream.close();
        fileChannel.close();
        writableByteChannel.close();
    }
}
