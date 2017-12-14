/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.dal.mapper;

import com.fulihui.sharding.jdbc.dal.do_.UserDO;

import java.util.List;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 15:00 yunfeng.li Exp $
 */
public interface UserMapper {

    Integer insert(UserDO u);

    List<UserDO> findAll();

    List<UserDO> findByUserIds(List<Integer> userIds);
}
