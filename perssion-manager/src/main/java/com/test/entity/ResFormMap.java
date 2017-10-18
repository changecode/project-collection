package com.test.entity;

import com.test.annotation.TableSeg;
import com.test.util.FormMap;


/**
 * 菜单实体表
 */
@TableSeg(tableName = "ly_resources", id = "id")
public class ResFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
