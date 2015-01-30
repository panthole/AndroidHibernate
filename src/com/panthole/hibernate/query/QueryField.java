/*
 * 文 件 名:  QueryFields.java
 * 版    权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2012,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  Shenxm
 * 修改时间:  2012-12-18
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate.query;

/**
 * 查询字段
 */
public class QueryField
{
    /**
     * = 相等查询
     */
    public static final String EQUAL = "=";

    /**
     * like 模糊匹配
     */
    public static final String LIKE = "like";

    /**
     * < 小于
     */
    public static final String LT = "<";

    /**
     * <= 小于等于
     */
    public static final String LE = "<=";

    /**
     * > 大于
     */
    public static final String GT = ">";

    /**
     * >= 大于等于
     */
    public static final String GE = ">=";

    /**
     * != 不等于
     */
    public static final String NE = "!=";

    /**
     * 查询类型，默认为相等查询
     */
    private String queryType = EQUAL;

    /**
     * 查询字段值
     */
    private Object value;

    /**
     * 返回 queryType
     * 
     * @return queryType
     */
    public String getQueryType()
    {
        return queryType;
    }

    /**
     * 对queryType进行赋值
     * 
     * @param queryType queryType
     */
    public void setQueryType(String queryType)
    {
        this.queryType = queryType;
    }

    /**
     * 返回 value
     * 
     * @return value
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * 对value进行赋值
     * 
     * @param value value
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
}