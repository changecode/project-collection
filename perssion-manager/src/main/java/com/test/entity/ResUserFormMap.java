package com.test.entity;

import com.test.annotation.TableSeg;
import com.test.util.FormMap;


/**
 * 实体表
 */
@TableSeg(tableName = "ly_res_user", id = "userId")
public class ResUserFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
