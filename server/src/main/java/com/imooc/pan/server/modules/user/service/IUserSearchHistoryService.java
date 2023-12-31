package com.imooc.pan.server.modules.user.service;

import com.imooc.pan.server.modules.user.context.QueryUserSearchHistoryContext;
import com.imooc.pan.server.modules.user.entity.driveHarborUserSearchHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pan.server.modules.user.vo.UserSearchHistoryVO;

import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_user_search_history(user search history table)】的数据库操作Service
* @createDate 2023-10-23 20:56:15
*/
public interface IUserSearchHistoryService extends IService<driveHarborUserSearchHistory> {

    List<UserSearchHistoryVO> getUserSearchHistories(QueryUserSearchHistoryContext context);
}
