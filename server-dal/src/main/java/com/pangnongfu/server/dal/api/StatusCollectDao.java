package com.pangnongfu.server.dal.api;

import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusCollect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
public interface StatusCollectDao extends JpaRepository<StatusCollect, Long> {
    List<StatusCollect> findByAccountCollect( Account accountCollect);
    Page<StatusCollect> findByAccountCollect(Account accountCollect, Pageable pageable);

    StatusCollect findByStatusAndAccountCollect(Status status, Account accountCollect);
}
