package com.test.entity;

import com.test.annotation.TableSeg;
import com.test.util.FormMap;

@TableSeg(tableName = "ly_server_info", id = "id")
public class ServerInfoFormMap extends FormMap<String, Object> {
    private static final long serialVersionUID = 1L;
}