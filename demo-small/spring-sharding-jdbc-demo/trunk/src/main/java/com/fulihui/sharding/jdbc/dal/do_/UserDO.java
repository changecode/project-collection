/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.dal.do_;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 14:54 yunfeng.li Exp $
 */
public class UserDO {

    private Integer id;

    private Integer userId;

    private String name;

    private Integer age;

    /**
     * Getter method for property <tt>id</tt>
     *
     * @return property value of id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     *
     * @param id value to be assigned to property id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>userId</tt>
     *
     * @return property value of userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Setter method for property <tt>userId</tt>.
     *
     * @param userId value to be assigned to property userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Getter method for property <tt>name</tt>
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>age</tt>
     *
     * @return property value of age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Setter method for property <tt>age</tt>.
     *
     * @param age value to be assigned to property age
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}
