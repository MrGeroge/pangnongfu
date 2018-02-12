package com.pangnongfu.server.dal.api;

import com.pangnongfu.server.dal.po.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xinghai on 2015/9/21.
 * Modified by zhangyu on 2015/9/23
 */
@Repository
public interface AccountDao extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

}
