/*
 * 文 件 名:  BasePO.java
 * 版    权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  shenxm
 * 修改时间:  2013-4-24
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate.po;

import com.panthole.hibernate.ahibernate.annotation.Column;
import com.panthole.hibernate.ahibernate.annotation.Id;

/**
 * 持久化对象基类
 */
public class BasePO
{
    /**
     * 主键,int类型,数据库建表时此字段会设为自增长
     */
    @Id
    @Column(name = "id")
    private int id;

    /**
     * 返回 id
     * 
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * 对id进行赋值
     * 
     * @param id id
     */
    public void setId(int id)
    {
        this.id = id;
    }
}