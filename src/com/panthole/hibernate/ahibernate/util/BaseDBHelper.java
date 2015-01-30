package com.panthole.hibernate.ahibernate.util;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * 数据库基础帮助类
 */
public class BaseDBHelper extends SQLiteOpenHelper {

	/**
	 * 持久化数据库对象
	 */
	private final Class<?>[] modelClasses;

	/**
	 * 默认构造方法
	 */
	public BaseDBHelper(Context context, String databaseName,
			SQLiteDatabase.CursorFactory factory, int databaseVersion,
			Class<?>[] modelClasses) {
		super(context, databaseName, factory, databaseVersion);
		this.modelClasses = modelClasses;
	}


	/** {@inheritDoc} */
	@Override
	public void onCreate(SQLiteDatabase db) {
		TableHelper.createTablesByClasses(db, this.modelClasses);
	}

	/** {@inheritDoc} */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TableHelper.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}

}