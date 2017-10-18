package com.test.mapper;

import com.test.entity.RoleFormMap;
import com.test.mapper.base.BaseMapper;

import java.util.List;

public interface RoleMapper extends BaseMapper {
    public List<RoleFormMap> seletUserRole(RoleFormMap map);

    public void deleteById(RoleFormMap map);
}
