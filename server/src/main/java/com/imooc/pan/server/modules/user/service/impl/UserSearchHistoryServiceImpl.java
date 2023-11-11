package com.imooc.pan.server.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pan.server.modules.user.context.QueryUserSearchHistoryContext;
import com.imooc.pan.server.modules.user.entity.driveHarborUserSearchHistory;
import com.imooc.pan.server.modules.user.service.IUserSearchHistoryService;
import com.imooc.pan.server.modules.user.mapper.driveHarborUserSearchHistoryMapper;
import com.imooc.pan.server.modules.user.vo.UserSearchHistoryVO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author benchi
* @description 针对表【r_pan_user_search_history(user search history table)】的数据库操作Service实现
* @createDate 2023-10-23 20:56:15
*/
@Service(value = "userSearchHistoryService")
public class UserSearchHistoryServiceImpl extends ServiceImpl<driveHarborUserSearchHistoryMapper, driveHarborUserSearchHistory>
    implements IUserSearchHistoryService {
    /**
     * 查询用户的搜索历史记录，默认十条
     *
     * @param context
     * @return
     */
    @Override
    public List<UserSearchHistoryVO> getUserSearchHistories(QueryUserSearchHistoryContext context) {
        return baseMapper.selectUserSearchHistories(context);
    }

}




