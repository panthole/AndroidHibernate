package com.panthole.hibernate.ahibernate.util;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sqlcipher.database.SQLiteDatabase;

import com.panthole.hibernate.HealthLog;
import com.panthole.hibernate.ahibernate.annotation.Column;
import com.panthole.hibernate.ahibernate.annotation.Id;
import com.panthole.hibernate.ahibernate.annotation.Table;

/**
 * 维护表帮助类
 */
public final class TableHelper
{
    /**
     * 日志对象
     */
    private static HealthLog log = HealthLog.getLog(TableHelper.class);

    /**
     * 根据多个持久化对象创建表
     * 
     * @param db sqlite数据库
     * @param clazzs 多个持久化对象类信息
     */
    public static <T> void createTablesByClasses(SQLiteDatabase db, Class<?>[] clazzs)
    {
        for (Class<?> clazz : clazzs)
        {
            createTable(db, clazz);
        }
    }

    /**
     * 根据多个持久化对象删除多个表
     * 
     * @param db sqlite数据库
     * @param clazzs 多个持久化对象类信息
     */
    public static <T> void dropTablesByClasses(SQLiteDatabase db, Class<?>[] clazzs)
    {
        for (Class<?> clazz : clazzs)
        {
            dropTable(db, clazz);
        }
    }

    /**
     * 根据持久化对象创建表
     * 
     * @param db sqlite数据库
     * @param clazz 持久化对象类信息
     */
    public static <T> void createTable(SQLiteDatabase db, Class<T> clazz)
    {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class))
        {
            Table table = clazz.getAnnotation(Table.class);
            tableName = table.name();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" ("); // 不存在表时才创建表

        List<Field> allFields = TableHelper.joinFields(clazz.getDeclaredFields(), clazz.getSuperclass()
                .getDeclaredFields());
        for (Field field : allFields)
        {
            if (!field.isAnnotationPresent(Column.class))
            {
                continue;
            }

            Column column = field.getAnnotation(Column.class);

            String columnType = "";
            if (null == column.type() || "".equals(column.type()))
            {
                columnType = getColumnType(field.getType());
            }
            else
            {
                columnType = column.type();
            }

            sb.append(column.name() + " " + columnType);

            if (column.length() != 0)
            {
                sb.append("(" + column.length() + ")");
            }

            // update 2012-06-10 实体类定义为Integer类型后不能生成Id异常
            if ((field.isAnnotationPresent(Id.class))
                    && ((field.getType() == Integer.TYPE) || (field.getType() == Integer.class)))
            {
                sb.append(" primary key autoincrement");
            }
            else if (field.isAnnotationPresent(Id.class))
            {
                sb.append(" primary key");
            }

            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append(')');

        String sql = sb.toString();

        log.debug("create table [", tableName, "]: ", sql);

        db.execSQL(sql);
    }

    /**
     * 根据持久化对象删除表
     * 
     * @param db sqlite数据库
     * @param clazz 持久化对象类信息
     */
    public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz)
    {
        String tableName = "";
        if (clazz.isAnnotationPresent(Table.class))
        {
            Table table = clazz.getAnnotation(Table.class);
            tableName = table.name();
        }
        String sql = "DROP TABLE IF EXISTS " + tableName;
        log.debug("dropTable[", tableName, "]:", sql);
        db.execSQL(sql);
    }

    /**
     * 获取字段数据类型
     * 
     * @param fieldType 字段类型
     * @return 数据类型字符串格式
     */
    private static String getColumnType(Class<?> fieldType)
    {
        if (String.class == fieldType)
        {
            return "TEXT";
        }
        if ((Integer.TYPE == fieldType) || (Integer.class == fieldType))
        {
            return "INTEGER";
        }
        if ((Long.TYPE == fieldType) || (Long.class == fieldType))
        {
            return "BIGINT";
        }
        if ((Float.TYPE == fieldType) || (Float.class == fieldType))
        {
            return "FLOAT";
        }
        if ((Short.TYPE == fieldType) || (Short.class == fieldType))
        {
            return "INT";
        }
        if ((Double.TYPE == fieldType) || (Double.class == fieldType))
        {
            return "DOUBLE";
        }
        if (Blob.class == fieldType)
        {
            return "BLOB";
        }

        return "TEXT";
    }

    /**
     * 合并Field数组并去重,并实现过滤掉非Column字段,和实现Id放在首字段位置功能
     * 
     * @param fields1 字段数组1
     * @param fields2 字段数组1
     * @return 合并和的字段数组
     */
    public static List<Field> joinFields(Field[] fields1, Field[] fields2)
    {
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        for (Field field : fields1)
        {
            // 过滤掉非Column定义的字段
            if (!field.isAnnotationPresent(Column.class))
            {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            map.put(column.name(), field);
        }
        for (Field field : fields2)
        {
            // 过滤掉非Column定义的字段
            if (!field.isAnnotationPresent(Column.class))
            {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (!map.containsKey(column.name()))
            {
                map.put(column.name(), field);
            }
        }
        List<Field> list = new ArrayList<Field>();
        for (String key : map.keySet())
        {
            Field tempField = map.get(key);
            // 如果是Id则放在首位置.
            if (tempField.isAnnotationPresent(Id.class))
            {
                list.add(0, tempField);
            }
            else
            {
                list.add(tempField);
            }
        }
        return list;
    }
}