package com.pangnongfu.server.bal.service;

import com.pangnongfu.server.bal.api.AccountService;
import com.pangnongfu.server.bal.dto.AccountDetailDTO;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.bal.utils.MD5Util;
import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.AccountDetailDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.AccountDetail;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by hao on 2015/9/23.
 */
public class AccountServiceMockTest {

    @Test
    public void testValidate()throws BizException{
        AccountDao accountDao = EasyMock.createMock(AccountDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);

        Account account=new Account();
        account.setUsername("admin");
        account.setPassword(MD5Util.encoding("888"));
        account.setId(1);

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setId(1);
        accountDetail.setAccount(account);

        EasyMock.expect(accountDao.findByUsername("admin")).andReturn(account);
        EasyMock.expect(accountDetailDao.findByAccount(account)).andReturn(accountDetail);
        EasyMock.replay(accountDao);
        EasyMock.replay(accountDetailDao);
        AccountService accountService = new AccountServiceImpl()
                .setAccountDao(accountDao)
                .setAccountDetailDao(accountDetailDao);

        AccountDetailDTO detailDTO = accountService.validate("admin", "888");
        Assert.assertEquals(1,detailDTO.getUserId());
    }

    @Test
    public void testUpdatePassword()throws BizException{
        /*AccountDao accountDao = EasyMock.createMock(AccountDao.class);

        Account account=new Account();
        account.setUsername("admin");
        account.setPassword(MD5Util.encoding("888"));
        account.setId(1);

        EasyMock.expect(accountDao.findOne((long)1)).andReturn(account);

        Account accountNew = new Account();
        accountNew.setId(1);
        accountNew.setUsername("admin");
        accountNew.setPassword(MD5Util.encoding("777"));

        EasyMock.expect(accountDao.save(account)).andReturn(accountNew);
        EasyMock.replay(accountDao);

        AccountService accountService = new AccountServiceImpl()
                .setAccountDao(accountDao);
        accountService.updatePassword(1, "777");
        Assert.assertEquals(MD5Util.encoding("777"),accountNew.getPassword());*/

        AccountDao accountDao = EasyMock.createMock(AccountDao.class);

        Account account=new Account();
        account.setUsername("admin");
        account.setPassword(MD5Util.encoding("888"));
        account.setId(1);

        EasyMock.expect(accountDao.findByUsername("admin")).andReturn(account);

        Account accountNew = new Account();
        accountNew.setId(1);
        accountNew.setUsername("admin");
        accountNew.setPassword(MD5Util.encoding("777"));

        EasyMock.expect(accountDao.save(account)).andReturn(accountNew);
        EasyMock.replay(accountDao);

        AccountService accountService = new AccountServiceImpl()
                .setAccountDao(accountDao);
        accountService.updatePassword("admin", "777");
        Assert.assertEquals(MD5Util.encoding("777"),accountNew.getPassword());
    }

    @Test
    public void testAddAccount()throws BizException{

        /**单元测试失败，因为nickname包含随机数*/

        AccountDao accountDao = EasyMock.createMock(AccountDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);

        Account account=new Account();
        account.setUsername("11111111111");
        account.setPassword(MD5Util.encoding("888"));

        Account account1 = new Account();
        account1.setId(1);
        account1.setUsername("11111111111");
        account1.setPassword(MD5Util.encoding("888"));

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setNickname("pnf_1");
        accountDetail.setPhone("11111111111");
        accountDetail.setAccount(account1);

        AccountDetail accountDetail1 = new AccountDetail();
        accountDetail1.setId(1);
        accountDetail1.setNickname("pnf_1");
        accountDetail1.setPhone("11111111111");
        accountDetail1.setAccount(account1);

        EasyMock.expect(accountDao.findByUsername("11111111111")).andReturn(null);
        EasyMock.expect(accountDao.save(account)).andReturn(account1);
        EasyMock.expect(accountDetailDao.save(accountDetail)).andReturn(accountDetail1);
        EasyMock.replay(accountDao);
        EasyMock.replay(accountDetailDao);

        AccountService accountService = new AccountServiceImpl()
                .setAccountDao(accountDao).
                setAccountDetailDao(accountDetailDao);

        long id = accountService.addAccount("11111111111","888");
        Assert.assertEquals(1,id);
    }

    @Test
    public void testUpdateAccountInfo()throws BizException{
        AccountDao accountDao = EasyMock.createMock(AccountDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("admin");
        //account.setPassword(MD5Util.encoding("888"));
        //EasyMock.expect(accountDao.findOne((long)1)).andReturn(account);

        AccountDetail detail = new AccountDetail();
        detail.setId(1);
        detail.setNickname("ch");
        detail.setAccount(account);
        EasyMock.expect(accountDetailDao.findByAccount(account)).andReturn(detail);

        EasyMock.expect(accountDetailDao.save(detail)).andReturn(detail);

        EasyMock.replay(accountDao);
        EasyMock.replay(accountDetailDao);

        AccountService accountService = new AccountServiceImpl().
                setAccountDao(accountDao).
                setAccountDetailDao(accountDetailDao);

        AccountDetailDTO accountDetailDTO = new AccountDetailDTO(1,"hao","abc","男","","","");
        accountService.updateAccountInfo(accountDetailDTO);

        Assert.assertEquals("hao",detail.getNickname());
    }
}
