package com.test.mapper;

import com.test.entity.UserFormMap;
import com.test.mapper.base.BaseMapper;

import java.util.List;


public interface UserMapper extends BaseMapper {

    public List<UserFormMap> findUserPage(UserFormMap userFormMap);

}
