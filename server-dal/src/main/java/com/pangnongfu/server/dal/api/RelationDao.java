package com.pangnongfu.server.dal.api;


import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Relation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xinghai on 2015/9/21.
 */
@Repository
public interface RelationDao extends JpaRepository <Relation, Long> {

    List<Relation> findByFollower(Account follower);
    List<Relation> findByFollowing(Account following);
    Page<Relation> findByFollower(Account follower, Pageable pageable);
    Page<Relation> findByFollowing(Account following, Pageable pageable);
    Relation findByFollowerAndFollowing(Account follower,Account following);
}
