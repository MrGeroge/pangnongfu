package com.pangnongfu.server.dal.api;


import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusCategory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
@Transactional
public interface StatusDao extends JpaRepository<Status, Long> {
    @Cacheable(value = "defaultCache",condition = "true")
    List<Status> findByStatusPublisher(Account statusPublisher);
    @Cacheable(value = "defaultCache",condition = "true")
    Page<Status> findByStatusPublisher(Account statusPublisher, Pageable pageable);
    @Cacheable(value ="defaultCache",condition = "true")
    Page<Status> findByStatusCategory(StatusCategory statusCategory, Pageable pageable);
}
