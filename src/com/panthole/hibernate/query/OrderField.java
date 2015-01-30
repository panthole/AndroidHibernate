/*
 * 文 件 名:  OrderFields.java
 * 版    权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  shenxm
 * 修改时间:  2013-1-22
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate.query;

/**
 * 排序字段
 */
public class OrderField
{
    /**
     * 降序
     */
    public static final String DESC = "desc";

    /**
     * 升序
     */
    public static final String ASC = "asc";

    /**
     * 排序方式：
     * DESC 降序
     * ASC 升序
     */
    private String orderType;

    /**
     * 排序字段
     */
    private String field;

    /**
     * 返回 orderType
     * 
     * @return orderType
     */
    public String getOrderType()
    {
        return orderType;
    }

    /**
     * 对orderType进行赋值
     * 
     * @param orderType orderType
     */
    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }

    /**
     * 返回 field
     * 
     * @return field
     */
    public String getField()
    {
        return field;
    }

    /**
     * 对field进行赋值
     * 
     * @param field field
     */
    public void setField(String field)
    {
        this.field = field;
    }
}