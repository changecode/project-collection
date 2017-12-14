/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.service;

import com.fulihui.sharding.jdbc.dal.do_.UserDO;

import java.util.List;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 15:09 yunfeng.li Exp $
 */
public interface UserService {

    public boolean insert(UserDO u);

    public List<UserDO> findAll();

    public List<UserDO> findByUserIds(List<Integer> ids);

    public void transactionTestSucess();

    public void transactionTestFailure() throws IllegalAccessException;
}
