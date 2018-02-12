package com.pangnongfu.server.web.vo;

/**
 * Created by shuiyu on 2015/9/26.
 */
public class AccountSimpleVO {
    private long account_id;
    private String nickname;
    private String avatar_url;

    public AccountSimpleVO(){}
    public AccountSimpleVO(long account_id,String nickname,String avatar_url){
        this.account_id = account_id;
        this.nickname=nickname;
        this.avatar_url = avatar_url;
    }
    public long getAccount_id() {
        return account_id;
    }

    public AccountSimpleVO setAccount_id(long account_id) {
        this.account_id = account_id;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public AccountSimpleVO setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public AccountSimpleVO setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
        return  this;
    }
}
