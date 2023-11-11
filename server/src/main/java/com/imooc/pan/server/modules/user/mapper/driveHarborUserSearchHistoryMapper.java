package com.imooc.pan.server.modules.user.mapper;

import com.imooc.pan.server.modules.user.context.QueryUserSearchHistoryContext;
import com.imooc.pan.server.modules.user.entity.driveHarborUserSearchHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pan.server.modules.user.vo.UserSearchHistoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_user_search_history(user search history table)】的数据库操作Mapper
* @createDate 2023-10-23 20:56:15
* @Entity com.imooc.pan.server.modules.user.entity.driveHarborUserSearchHistory
*/
public interface driveHarborUserSearchHistoryMapper extends BaseMapper<driveHarborUserSearchHistory> {

    List<UserSearchHistoryVO> selectUserSearchHistories(@Param("param") QueryUserSearchHistoryContext context);
}




