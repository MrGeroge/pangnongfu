package com.pangnongfu.server.dal.po;

import javax.persistence.*;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="status_love")
@Table(name="tb_status_love")
public class StatusLove {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account accountLove;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="status_id")
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccountLove() {
        return accountLove;
    }

    public void setAccountLove(Account accountLove) {
        this.accountLove = accountLove;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusLove that = (StatusLove) o;

        if (id != that.id) return false;
        if (accountLove != null ? !accountLove.equals(that.accountLove) : that.accountLove != null) return false;
        return !(status != null ? !status.equals(that.status) : that.status != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        return result;
    }

}
