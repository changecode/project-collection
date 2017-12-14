/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.service.impl;

import com.fulihui.sharding.jdbc.dal.do_.StudentDO;
import com.fulihui.sharding.jdbc.dal.mapper.StudentMapper;
import com.fulihui.sharding.jdbc.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 15:07 yunfeng.li Exp $
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    public boolean insert(StudentDO student) {
        return studentMapper.insert(student) > 0 ? true:false;
    }
}
