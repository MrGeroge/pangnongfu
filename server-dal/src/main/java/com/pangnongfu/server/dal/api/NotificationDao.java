package com.pangnongfu.server.dal.api;


import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
public interface NotificationDao  extends JpaRepository<Notification, Long> {
    List< Notification> findByConsumer(Account consumer);
}
