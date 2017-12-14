/**
 * fulihui.com Inc.
 * Copyright (c) 2015-2016 All Rights Reserved.
 */
package com.fulihui.sharding.jdbc.dal.do_;

/**
 * @author yunfeng.li
 * @version $Id: v 0.1 2017年04月09日 14:58 yunfeng.li Exp $
 */
public class StudentDO{

    private Integer id;

    private Integer studentId;

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
     * Getter method for property <tt>studentId</tt>
     *
     * @return property value of studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * Setter method for property <tt>studentId</tt>.
     *
     * @param studentId value to be assigned to property studentId
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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
