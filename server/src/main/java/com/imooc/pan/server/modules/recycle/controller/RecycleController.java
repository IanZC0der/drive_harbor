package com.imooc.pan.server.modules.recycle.controller;

import com.imooc.pan.core.response.R;
import com.imooc.pan.server.common.utils.UserIdUtil;
import com.imooc.pan.server.modules.file.vo.DriveHarborUserFileVO;
import com.imooc.pan.server.modules.recycle.context.QueryRecycleFileListContext;
import com.imooc.pan.server.modules.recycle.service.IRecycleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Recycler module")
@Validated
public class RecycleController {
    @Autowired
    private IRecycleService iRecycleService;

    @ApiOperation(
            value = "query files list in the trash can",
            notes = "This interface provides the functionality of querying the file list in the trash can",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("recycles")
    public R<List<DriveHarborUserFileVO>> recycles() {
        QueryRecycleFileListContext context = new QueryRecycleFileListContext();
        context.setUserId(UserIdUtil.get());
        List<DriveHarborUserFileVO> result = iRecycleService.recycles(context);
        return R.data(result);
    }
}
