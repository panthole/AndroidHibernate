package com.panthole.hibernate.ahibernate.dao;

import java.util.List;
import java.util.Map;


import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteOpenHelper;

import com.panthole.hibernate.query.Condition;

/**
 * 数据访问层基类接口
 */
public interface BaseDao<T> {
	/**
	 * 获取sqlite数据库打开帮助类
	 * 
	 * @return sqlite数据库打开帮助类
	 */
//	SQLiteOpenHelper getDbHelper();

	/**
	 * 默认主键自增,调用insert(T,true);
	 * 
	 * @param entity
	 * @return 主键
	 */
	long insert(T entity);

	/**
	 * 批量插入实体类
	 * 
	 * @param List
	 *            <T> entity
	 * @param flag
	 *            true 自动生成主键 false 需手工指定主键
	 * @return 主键
	 */
	long bulkInsert(List<T> entity, boolean flag);

	Cursor search(String inputText);

	/**
	 * 插入实体类
	 * 
	 * @param entity
	 * @param flag
	 *            true 自动生成主键 false 需手工指定主键
	 * @return 主键
	 */
	long insert(T entity, boolean flag);

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 *            int数据类型的主键
	 */
	void delete(int id);

	/**
	 * 根据主键删除记录
	 * 
	 * @param id
	 *            Integer数据类型的主键
	 */
	void delete(Integer... id);

	/**
	 * 根据指定列名称，删除多行记录
	 * 
	 * @param column
	 *            指定列名称
	 * @param value
	 *            用户ID
	 */
	void deleteByAppointCol(String column, String value);

	/**
	 * 根据指定列名称，删除多行记录
	 * 
	 * @param column
	 *            指定列名称
	 * @param values
	 *            多行记录取值
	 */
	void deleteByAppointCol(String column, Object... values);

	/**
	 * 更新记录
	 * 
	 * @param entity
	 *            持久化对象
	 */
	void update(T entity);

	/**
	 * 根据主键获取持久化对象
	 * 
	 * @param id
	 *            int数据类型的主键
	 * @return 持久化对象
	 */
	T get(int id);

	/**
	 * 基本sql查询
	 * 
	 * @param sql
	 *            SQL
	 * @param selectionArgs
	 *            指定返回字段
	 * @return 持久化对象列表
	 */
	List<T> rawQuery(String sql, String[] selectionArgs);

	/**
	 * 获取所有持久化对象
	 * 
	 * @return 所有持久化对象
	 */
	List<T> find();

	/**
	 * 根据条件查询
	 * 
	 * @param columns
	 *            columns
	 * @param selection
	 *            selection
	 * @param selectionArgs
	 *            selectionArgs
	 * @param groupBy
	 *            groupBy
	 * @param having
	 *            having
	 * @param orderBy
	 *            orderBy
	 * @param limit
	 *            limit
	 * @return 持久化对象集合
	 */
	List<T> find(String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit);

	boolean isExist(String sql, String[] selectionArgs);

	/**
	 * 指定查询返回的数据库字段，将查询的结果保存为键值对的map
	 * 
	 * @param sql
	 *            查询sql
	 * @param selectionArgs
	 *            参数值
	 * @return 返回的Map中的key全部是小写形式.
	 */
	List<Map<String, String>> query2MapList(String sql, String[] selectionArgs);

	/**
	 * 封装执行sql代码.
	 * 
	 * @param sql
	 *            SQL
	 * @param selectionArgs
	 *            需要返回的字段
	 */
	void execSql(String sql, Object[] selectionArgs);

	/**
	 * 根据条件查询
	 * 
	 * @param condition
	 *            封装的查询条件
	 * @param userStatus
	 *            是否加入状态条件
	 * @return 持久化对象集合
	 */
	List<T> query(Condition condition, boolean userStatus);

	/**
	 * 根据条件搜索
	 * 
	 * @param condition
	 *            封装的查询条件
	 * @param isSingle
	 *            是否只有一条相似条件
	 * @return 持久化对象集合
	 */
	List<T> search(String userId, String queryText, String msgGrid);

	/**
	 * 获取最后更新的时间戳
	 * 
	 * @param byUserId
	 *            是否根据用户ID获取最新时间戳
	 * @return 最后更新的时间戳
	 */
	String latestTimestamp(boolean byUserId);

	void update(T entity, int id);

	void deleteUser(String userId);

	List<T> searchOneColumn(String queryText, String table, String column);
}