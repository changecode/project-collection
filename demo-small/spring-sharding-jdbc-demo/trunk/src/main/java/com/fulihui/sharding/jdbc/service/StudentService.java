/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.service;

import com.fulihui.sharding.jdbc.dal.do_.StudentDO;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 15:06 yunfeng.li Exp $
 */
public interface StudentService {

    boolean insert(StudentDO student);
}
