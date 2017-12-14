/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.dal.mapper;

import com.fulihui.sharding.jdbc.dal.do_.StudentDO;

import java.util.List;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 14:59 yunfeng.li Exp $
 */
public interface StudentMapper {

    Integer insert(StudentDO s);

    List<StudentDO> findAll();

    List<StudentDO> findByStudentIds(List<Integer> studentIds);
}
