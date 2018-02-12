package com.pangnongfu.server.dal.po;

import javax.persistence.*;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="relation")
@Table(name="tb_relation")
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="follower_id")
    private Account follower;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="following_id")
    private Account following;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getFollower() {
        return follower;
    }

    public void setFollower(Account follower) {
        this.follower = follower;
    }

    public Account getFollowing() {
        return following;
    }

    public void setFollowing(Account following) {
        this.following = following;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relation relation = (Relation) o;

        if (id != relation.id) return false;
        if (follower != null ? !follower.equals(relation.follower) : relation.follower != null) return false;
        return !(following != null ? !following.equals(relation.following) : relation.following != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        return result;
    }

}
