package com.pangnongfu.server.dal.api;

import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
public interface StatusCommentDao extends JpaRepository<StatusComment, Long> {
    List<StatusComment> findByCommentPublisher( Account commentPublisher);
    List<StatusComment> findByStatus(Status status);
}
