package com.pangnongfu.server.dal.api;


import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
public interface StatusImageDao extends JpaRepository<StatusImage, Long> {

    List<StatusImage> findByStatus(Status status);
}
