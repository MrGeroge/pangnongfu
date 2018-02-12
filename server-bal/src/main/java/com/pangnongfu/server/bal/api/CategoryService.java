package com.pangnongfu.server.bal.api;

import com.pangnongfu.server.bal.dto.AccountDetailDTO;
import com.pangnongfu.server.bal.dto.StatusCategoryDTO;
import com.pangnongfu.server.bal.exception.BizException;

/**
 * Author:shuiyu
 * create on 15/10/8.
 */
public interface CategoryService {
    /**
     * 获取状态类别的列表
     * @return
     */
     StatusCategoryDTO listCategory();
}
