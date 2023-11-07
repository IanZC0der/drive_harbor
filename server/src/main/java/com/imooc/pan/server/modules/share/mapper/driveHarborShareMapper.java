package com.imooc.pan.server.modules.share.mapper;

import com.imooc.pan.server.modules.share.entity.driveHarborShare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pan.server.modules.share.vo.DriveHarborShareUrlListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_share(User_Sharing_History_Table)】的数据库操作Mapper
* @createDate 2023-10-23 21:08:08
* @Entity com.imooc.pan.server.modules.share.entity.driveHarborShare
*/
public interface driveHarborShareMapper extends BaseMapper<driveHarborShare> {

    /**
     * query share list
     * @param userId
     * @return
     */
    List<DriveHarborShareUrlListVO> selectShareVOListByUserId(@Param("userId") Long userId);

    /**
     * rolling query share id
     * @param startId
     * @param limit
     * @return
     */
    List<Long> rollingQueryShareId(@Param("startId") long startId, @Param("limit") long limit);
}




