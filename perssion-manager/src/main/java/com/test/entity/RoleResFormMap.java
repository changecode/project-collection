package com.test.entity;

import com.test.annotation.TableSeg;
import com.test.util.FormMap;


/**
 * 实体表
 */
@TableSeg(tableName = "ly_role_res", id = "roleId")
public class RoleResFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
