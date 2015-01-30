/*
 * 文 件 名:  HealthPO.java
 * 版    权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  shenxm
 * 修改时间:  2013-5-4
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate.po;

import com.panthole.hibernate.ahibernate.annotation.Column;
import com.panthole.hibernate.ahibernate.annotation.Id;

/**
 * 持久化对象扩展基类(用于带有时间戳的持久化对象)
 */
public class HealthPO
{
    /**
     * 主键,int类型,数据库建表时此字段会设为自增长
     */
    @Id
    @Column(name = "id")
    private int id;

    /**
     * 数据创建时间：取系统当前时间(yyyyMMddHHmmss)
     */
    @Column(name = "CREATE_TIME", length = 14)
    private String createTime;

    /**
     * 数据修改时间：取系统当前时间(yyyyMMddHHmmss)
     */
    @Column(name = "UPDATE_TIME", length = 14)
    private String updateTime;

    /**
     * 当前数据状态
     * 0 新增
     * 1 修改
     * 9 删除
     */
    @Column(name = "STATUS", length = 1)
    private String status;

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

    /**
     * 返回 createTime
     * 
     * @return createTime
     */
    public String getCreateTime()
    {
        return createTime;
    }

    /**
     * 对createTime进行赋值
     * 
     * @param createTime createTime
     */
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    /**
     * 返回 updateTime
     * 
     * @return updateTime
     */
    public String getUpdateTime()
    {
        return updateTime;
    }

    /**
     * 对updateTime进行赋值
     * 
     * @param updateTime updateTime
     */
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    /**
     * 返回 status
     * 
     * @return status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * 对status进行赋值
     * 
     * @param status status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
}