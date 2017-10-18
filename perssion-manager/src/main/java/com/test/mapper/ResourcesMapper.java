package com.test.mapper;

import com.test.entity.ResFormMap;
import com.test.mapper.base.BaseMapper;

import java.util.List;

public interface ResourcesMapper extends BaseMapper {
    public List<ResFormMap> findChildlists(ResFormMap map);

    public List<ResFormMap> findRes(ResFormMap map);

    public void updateSortOrder(List<ResFormMap> map);

    public List<ResFormMap> findUserResourcess(String userId);

}
