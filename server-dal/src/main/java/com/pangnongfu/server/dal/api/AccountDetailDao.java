package com.pangnongfu.server.dal.api;

import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
public interface AccountDetailDao extends JpaRepository<AccountDetail, Long> {
    AccountDetail findByAccount(Account account);

}
