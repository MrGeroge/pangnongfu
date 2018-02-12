package com.pangnongfu.server.dal.api;

import com.pangnongfu.server.dal.po.StatusCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
public interface StatusCategoryDao extends JpaRepository<StatusCategory, Long> {


}
