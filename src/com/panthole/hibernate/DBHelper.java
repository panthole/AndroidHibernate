package com.panthole.hibernate;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.Context;

import com.panthole.hibernate.ahibernate.util.BaseDBHelper;
import com.panthole.hibernate.po.custom.CustInst;
import com.panthole.hibernate.po.custom.CustUser;

/**
 * 数据库帮助类
 */
public class DBHelper extends BaseDBHelper {

	private static DBHelper dbHelper;

	private static SQLiteDatabase dbRead;

	private static SQLiteDatabase dbWrite;

	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "HealthGrid.db";

	/**
	 * 数据库版本
	 */
	private static final int DBVERSION = 1;

	/**
	 * 数据库密码
	 */
	private static final String PASSWORD = "healthgrid01";

	/**
	 * 要初始化的表
	 */
	private static final Class<?>[] clazz = { CustInst.class, CustUser.class };

	/**
	 * 默认构造函数
	 */
	private DBHelper(Context context) {
		super(context, DB_NAME, null, DBVERSION, clazz);
	}

	/**
	 * 构造单例
	 */
	public static SQLiteDatabase getInstanceReadable(Context context) {
		if (dbRead == null) {
			// 指定数据库名为student，需修改时在此修改；此处使用默认工厂；指定版本为1
			dbHelper = new DBHelper(context);
			dbRead = dbHelper.getReadableDatabase(PASSWORD);
		}
		return dbRead;
	}

	/**
	 * 构造单例
	 */
	public static SQLiteDatabase getInstancedWritable(Context context) {
		if (dbWrite == null) {
			// 指定数据库名为student，需修改时在此修改；此处使用默认工厂；指定版本为1
			dbHelper = new DBHelper(context);
			dbWrite = dbHelper.getWritableDatabase(PASSWORD);
		}
		return dbWrite;
	}

}