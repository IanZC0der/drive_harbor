package com.imooc.pan.server.modules.file.mapper;

import com.imooc.pan.server.modules.file.context.QueryFileListContext;
import com.imooc.pan.server.modules.file.entity.driveHarborUserFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_user_file(user file information table)】的数据库操作Mapper
* @createDate 2023-10-23 21:04:43
* @Entity com.imooc.pan.server.modules.file.entity.driverHarborUserFile
*/
public interface driverHarborUserFileMapper extends BaseMapper<driveHarborUserFile> {

    /**
     * query files of a specific type
     * @param context
     * @return
     */
    List<DriveHarborUserFileVO> selectFileList(@Param("param") QueryFileListContext context);
}




