/*
 * 文 件 名:  Condition.java
 * 版    权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2012,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  Shenxm
 * 修改时间:  2012-12-13
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.panthole.hibernate.constants.Constants.Num4Int;
import com.panthole.hibernate.HealthUtils;

/**
 * 查询条件
 */
public class Condition
{
    /**
     * 当前页
     */
    private int pageNum = 1;

    /**
     * 每页显示记录数
     */
    private int numPerPage = Num4Int.NUM_20;

    /**
     * 是否分页查询，默认是分页查询的。
     */
    private boolean needPaging = true;

    /**
     * 指定查询返回对象，select后面指定返回的对象
     */
    private Class<?>[] selectObj;

    /**
     * 多表查询时，where后面的关联字段设置，
     * 如：user.userId = linkman.userId
     */
    private String[] relevanceFields;

    /**
     * 自定义查询条件
     */
    private String customizedWhere;

    /**
     * 记录总数
     */
    private int total;

    /**
     * 查询条件
     * 如果是多表查询，查询key需要加上别名，如：user.userName
     */
    private Map<String, QueryField> queryFields;

    /**
     * 排序字段
     */
    private List<OrderField> orderFields;

    /**
     * 查询条件集合容量
     */
    private int capacity;

    /**
     * 是否只获取总数 只提供count(*)查询
     */
    private boolean count;

    /**
     * 系统自动创建对象时，默认查询条件集合大小为1
     */
    public Condition()
    {
        super();
        this.capacity = 1;
    }

    /**
     * 设置查询条件容器大小
     * 
     * @param capacity capacity
     */
    public Condition(int capacity)
    {
        super();
        this.capacity = capacity;
    }

    /**
     * 构建查询对象时，可以决定是否需要分页查询
     * 
     * @param needPaging needPaging
     */
    public Condition(boolean needPaging)
    {
        super();
        this.needPaging = needPaging;
    }

    /**
     * 返回 pageNum
     * 
     * @return pageNum
     */
    public int getPageNum()
    {
        return pageNum;
    }

    /**
     * 对pageNum进行赋值
     * 
     * @param pageNum pageNum
     */
    public void setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
    }

    /**
     * 返回 numPerPage
     * 
     * @return numPerPage
     */
    public int getNumPerPage()
    {
        return numPerPage;
    }

    /**
     * 对numPerPage进行赋值
     * 
     * @param numPerPage numPerPage
     */
    public void setNumPerPage(int numPerPage)
    {
        this.numPerPage = numPerPage;
    }

    /**
     * 返回 needPaging
     * 
     * @return needPaging
     */
    public boolean isNeedPaging()
    {
        return needPaging;
    }

    /**
     * 返回 count
     * 
     * @return count
     */
    public boolean isCount()
    {
        return count;
    }

    /**
     * 对count进行赋值
     * 
     * @param count count
     */
    public void setCount(boolean count)
    {
        this.count = count;
    }

    /**
     * 对needPaging进行赋值
     * 
     * @param needPaging needPaging
     */
    public void setNeedPaging(boolean needPaging)
    {
        this.needPaging = needPaging;
    }

    /**
     * 返回 selectObj
     * 
     * @return selectObj
     */
    public Class<?>[] getSelectObj()
    {
        Class<?>[] temp = selectObj;

        return temp;
    }

    /**
     * 对selectObj进行赋值
     * 
     * @param selectObj selectObj
     */
    public void setSelectObj(Class<?>... selectObj)
    {
        if (null != selectObj && selectObj.length > 0)
        {
            Class<?>[] temp = new Class<?>[selectObj.length];
            int index = 0;
            for (Class<?> select : selectObj)
            {
                temp[index++] = select;
            }

            this.selectObj = temp;
        }
    }

    /**
     * 返回 relevanceFields
     * 
     * @return relevanceFields
     */
    public String[] getRelevanceFields()
    {
        String[] temp = relevanceFields;

        return temp;
    }

    /**
     * 对relevanceFields进行赋值
     * 
     * @param relevanceFields relevanceFields
     */
    public void setRelevanceFields(String... relevanceFields)
    {
        if (null != relevanceFields && relevanceFields.length > 0)
        {
            String[] temp = new String[relevanceFields.length];
            int index = 0;
            for (String relevanceField : relevanceFields)
            {
                temp[index++] = relevanceField;
            }

            this.relevanceFields = temp;
        }
    }

    /**
     * 返回 total
     * 
     * @return total
     */
    public int getTotal()
    {
        return total;
    }

    /**
     * 对total进行赋值
     * 
     * @param total total
     */
    public void setTotal(int total)
    {
        this.total = total;
    }

    /**
     * 返回 queryFields
     * 
     * @return queryFields
     */
    public Map<String, QueryField> getQueryFields()
    {
        return queryFields;
    }

    /**
     * 返回 orderFields
     * 
     * @return orderFields
     */
    public List<OrderField> getOrderFields()
    {
        return orderFields;
    }

    /**
     * 返回 customizedWhere
     * 
     * @return customizedWhere
     */
    public String getCustomizedWhere()
    {
        return customizedWhere;
    }

    /**
     * 对customizedWhere进行赋值
     * 
     * @param customizedWhere customizedWhere
     */
    public void setCustomizedWhere(String customizedWhere)
    {
        this.customizedWhere = customizedWhere;
    }

    /**
     * 对capacity进行赋值
     * 
     * @param capacity capacity
     */
    protected void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    /**
     * 设置查询条件集合
     * 
     * @param fieldName 查询字段名称
     * @param keyValue 查询字段值
     * @param queryType 查询方式
     */
    public void setQueryField(String fieldName, Object keyValue, String queryType)
    {
        dealQueryField(fieldName, keyValue, queryType);
    }

    /**
     * 设置查询条件集合（相等查询）
     * 
     * @param fieldName 查询字段名称
     * @param keyValue 查询字段值
     */
    public void setQueryField(String fieldName, Object keyValue)
    {
        dealQueryField(fieldName, keyValue, null);
    }

    /**
     * 设置排序字段
     * 
     * @param orderField 排序字段
     * @param orderType 排序方式
     */
    public void setOrderField(String orderField, String orderType)
    {
        // 先判断排序字段集合是否为空，如果为空，则新建。
        if (null == this.orderFields)
        {
            // 新建排序集合，默认大小为3。
            this.orderFields = new ArrayList<OrderField>(Num4Int.NUM_3);
        }

        OrderField orderFieldObj = new OrderField();
        orderFieldObj.setField(orderField);
        orderFieldObj.setOrderType(orderType);

        this.orderFields.add(orderFieldObj);
    }

    /**
     * 清除查询条件
     */
    public void clearQueryFields()
    {
        if (null != queryFields)
        {
            queryFields.clear();
        }
    }

    /**
     * 处理查询字段
     * 
     * @param fieldName 查询字段名称
     * @param keyValue 查询字段值
     * @param queryType 查询方式
     */
    private void dealQueryField(String fieldName, Object keyValue, String queryType)
    {
        // 先判断放查询条件的容器是否存在，不存在则新建。
        if (null == this.queryFields)
        {
            // 默认情况下，有几个查询条件，容量就设置为多少。
            this.queryFields = new HashMap<String, QueryField>(this.capacity);
        }

        if (null != keyValue && HealthUtils.isNotBlank(keyValue.toString()))
        {
            // 新建查询对象
            QueryField queryField = new QueryField();

            // 设置查询方式，如果没有设置，默认为相等查询。
            if (null != queryType)
            {
                queryField.setQueryType(queryType);
            }

            // 设置查询值
            queryField.setValue(keyValue);

            // 将查询条件加入查询条件容器中
            this.queryFields.put(fieldName, queryField);
        }
        else
        {
            // 没有当前查询条件，从容器中删除
            this.queryFields.remove(fieldName);
        }
    }
}