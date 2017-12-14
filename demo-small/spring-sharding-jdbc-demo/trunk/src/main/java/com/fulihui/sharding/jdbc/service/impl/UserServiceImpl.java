/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.service.impl;

import com.fulihui.sharding.jdbc.dal.do_.StudentDO;
import com.fulihui.sharding.jdbc.dal.do_.UserDO;
import com.fulihui.sharding.jdbc.dal.mapper.StudentMapper;
import com.fulihui.sharding.jdbc.dal.mapper.UserMapper;
import com.fulihui.sharding.jdbc.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 15:10 yunfeng.li Exp $
 */
@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private DataSource shardingDataSource;

    public boolean insert(UserDO user) {
        return userMapper.insert(user) > 0 ? true :false;
    }

    public List<UserDO> findAll() {
        return userMapper.findAll();
    }

    public List<UserDO> findByUserIds(List<Integer> ids) {
        return userMapper.findByUserIds(ids);
    }

    public void transactionTestSucess() {
        UserDO u = new UserDO();
        u.setUserId(10);
        u.setAge(25);
        u.setName("1111111111111111");
        userMapper.insert(u);

        UserDO u1 = new UserDO();
        u1.setUserId(21);
        u1.setAge(20);
        u1.setName("222222222222222");
        userMapper.insert(u1);

        StudentDO student = new StudentDO();
        student.setStudentId(31);
        student.setAge(21);
        student.setName("333333333333333333");
        studentMapper.insert(student);

        StudentDO student1 = new StudentDO();
        student1.setStudentId(30);
        student1.setAge(21);
        student1.setName("303030303030303");
        studentMapper.insert(student1);
    }

    public void transactionTestFailure() throws IllegalAccessException {

        UserDO u = new UserDO();
        u.setUserId(10);
        u.setAge(25);
        u.setName("1111111111111111");
        userMapper.insert(u);

        UserDO u1 = new UserDO();
        u1.setUserId(21);
        u1.setAge(20);
        u1.setName("222222222222222");
        userMapper.insert(u1);

        StudentDO student = new StudentDO();
        student.setStudentId(31);
        student.setAge(21);
        student.setName("333333333333333333");
        studentMapper.insert(student);

        StudentDO student1 = new StudentDO();
        student1.setStudentId(30);
        student1.setAge(21);
        student1.setName("303030303030303");
        studentMapper.insert(student1);

        throw new IllegalAccessException();

    }


}
