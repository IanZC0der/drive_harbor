package com.imooc.pan.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.imooc.pan.core.constants.driveHarborConstants;
import com.imooc.pan.core.exception.driveHarborBusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Global SnowFlake ID generator
 */
public class IdUtil {

    /**
     * machine id
     */
    private static long workerId;

    /**
     * data center id
     */
    private static long dataCenterId;

    /**
     * serial number
     */
    private static long sequence;

    /**
     * start time stamp
     */
    private static long startTimestamp = 1288834974657L;

    /**
     * length of workerID
     */
    private static long workerIdBits = 5L;

    /**
     * length of dataCenterID
     */
    private static long dataCenterIdBits = 5L;

    /**
     * max value of workerID
     */
    private static long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * max value of dataCenterID
     */
    private static long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * length of serial number
     */
    private static long sequenceBits = 12L;

    /**
     * max value of serial number
     */
    private static long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * left shift times of workerID
     */
    private static long workerIdShift = sequenceBits;

    /**
     * left shift times of dataCenterID
     */
    private static long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * left shift times for time stamp
     */
    private static long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * last time stamp
     */
    private static long lastTimestamp = -1L;

    static {
        workerId = getMachineNum() & maxWorkerId;
        dataCenterId = getMachineNum() & maxDataCenterId;
        sequence = 0L;
    }

    /**
     * acquire machineID
     *
     * @return machine id
     */
    private static long getMachineNum() {
        long machinePiece;
        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (e.hasMoreElements()) {
            NetworkInterface ni = e.nextElement();
            sb.append(ni.toString());
        }
        machinePiece = sb.toString().hashCode();
        return machinePiece;
    }

    /**
     * acquire time stamp and compare it with the last time stamp
     *
     * @param lastTimestamp
     * @return
     */
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * acquire system time stamp
     *
     * @return
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * generate id
     *
     * @return
     */
    public synchronized static Long get() {
        long timestamp = timeGen();
        // throw exception if current time stamp < last time stamp
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // sequence = sequence + 1 if lastTimestamp == timestamp. otherwise starts from 0
        // 0 - 4095
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        //将上次时间戳值刷新
        lastTimestamp = timestamp;

        /**
         * return result
         * (timestamp - startTimeStamp) << timestampLeftShift), left shift
         * (datacenterId << datacenterIdShift), left shift dataCenterID
         * (workerId << workerIdShift) leftShift workerID
         */
        return ((timestamp - startTimestamp) << timestampLeftShift) |
                (dataCenterId << dataCenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    /**
     * enrypting id
     *
     * @return
     */
    public static String encrypt(Long id) {
        if (Objects.nonNull(id)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            byteBuffer.putLong(0, id);
            byte[] content = byteBuffer.array();
            byte[] encrypt = AES128Util.aesEncrypt(content);
            return Base64.encode(encrypt);
        }
        return StringUtils.EMPTY;
    }

    /**
     * decrypting id
     *
     * @param decryptId
     * @return
     */
    public static Long decrypt(String decryptId) {
        if (StringUtils.isNotBlank(decryptId)) {
            byte[] encrypt = Base64.decode(decryptId);
            byte[] content = AES128Util.aesDecode(encrypt);
            if (ArrayUtil.isNotEmpty(content)) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(content);
                return byteBuffer.getLong();
            }
            throw new driveHarborBusinessException("AES128Util.aesDecode fail");
        }
        throw new driveHarborBusinessException("the decryptId can not be empty");
    }

    /**
     * decrypting the whole id
     *
     * @param decryptIdStr
     * @return
     */
    public static List<Long> decryptIdList(String decryptIdStr) {
        if (StringUtils.isBlank(decryptIdStr)) {
            return Lists.newArrayList();
        }
        List<String> decryptIdList = Splitter.on(driveHarborConstants.COMMON_SEPARATOR).splitToList(decryptIdStr);
        if (CollectionUtils.isEmpty(decryptIdList)) {
            return Lists.newArrayList();
        }
        List<Long> result = decryptIdList.stream().map(IdUtil::decrypt).collect(Collectors.toList());
        return result;
    }

    public static void main(String[] args) {
        System.out.println(encrypt(1721677497889308672L));
        System.out.println(encrypt(get()));
    }



}
