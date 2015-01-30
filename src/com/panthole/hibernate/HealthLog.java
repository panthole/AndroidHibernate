/*
 * 文 件  名:  HealthLog.java
 * 版       权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2014,  All rights reserved
 * 描       述:   <描述>
 * 修 改  人:   潘凌越
 * 修改时间:  2014-7-28 下午06:23:43
 * 版 本   号:  Version 2.1.0 
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate;

import com.panthole.hibernate.constants.Constants.Num4Int;

import android.util.Log;

/**
 * 日志封装
 */
public final class HealthLog
{
    /**
     * 类名，作为log中的tag使用
     */
    private static String className;

    /**
     * 获取日志对象
     * 
     * @param cls 类定义
     * @return 日志对象
     */
    public static HealthLog getLog(Class<?> cls)
    {
        HealthLog log = new HealthLog();

        className = cls.getSimpleName();

        return log;
    }

    /**
     * 调试日志
     * 
     * @param msgs 日志信息数组
     */
    public void debug(String... msgs)
    {
        Log.d(className, getMsg(msgs));
    }

    /**
     * 调试日志
     * 
     * @param tr 异常
     * @param msgs 日志信息数组
     */
    public void debug(Throwable tr, String... msgs)
    {
        Log.d(className, getMsg(msgs), tr);
    }

    /**
     * 异常日志
     * 
     * @param msgs 日志信息数组
     */
    public void error(String... msgs)
    {
        Log.e(className, getMsg(msgs));
    }

    /**
     * 异常日志
     * 
     * @param tr 异常
     * @param msgs 日志信息数组
     */
    public void error(Throwable tr, String... msgs)
    {
        Log.e(className, getMsg(msgs), tr);
    }

    /**
     * 获取信息
     * 
     * @param msgs 日志信息数组
     * @return 日志信息
     */
    private String getMsg(String... msgs)
    {
        StringBuffer msgBuffer = new StringBuffer(Num4Int.NUM_100);

        if (HealthUtils.isNotEmpty(msgs))
        {
            for (String msg : msgs)
            {
                msgBuffer.append(msg);
            }
        }

        return msgBuffer.toString();
    }
}