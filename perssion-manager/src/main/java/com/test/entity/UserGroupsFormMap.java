package com.test.entity;

import com.test.annotation.TableSeg;
import com.test.util.FormMap;


/**
 * 实体表
 */
@TableSeg(tableName = "ly_user_role", id = "userId")
public class UserGroupsFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
