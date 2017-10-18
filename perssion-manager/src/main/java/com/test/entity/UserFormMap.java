package com.test.entity;

import com.test.annotation.TableSeg;
import com.test.util.FormMap;


/**
 * user实体表
 */
@TableSeg(tableName = "ly_user", id = "id")
public class UserFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
