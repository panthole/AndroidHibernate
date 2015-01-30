package com.panthole.hibernate.ahibernate.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.panthole.hibernate.DBHelper;
import com.panthole.hibernate.HealthLog;
import com.panthole.hibernate.HealthUtils;
import com.panthole.hibernate.ahibernate.annotation.Column;
import com.panthole.hibernate.ahibernate.annotation.Id;
import com.panthole.hibernate.ahibernate.annotation.Table;
import com.panthole.hibernate.ahibernate.dao.BaseDao;
import com.panthole.hibernate.ahibernate.util.TableHelper;
import com.panthole.hibernate.constants.Constants;
import com.panthole.hibernate.constants.Constants.DataStatus;
import com.panthole.hibernate.constants.Constants.Num4Int;
import com.panthole.hibernate.constants.TableColumn.TBasePO;
import com.panthole.hibernate.constants.TableColumn.TSgUser;
import com.panthole.hibernate.query.Condition;
import com.panthole.hibernate.query.OrderField;
import com.panthole.hibernate.query.QueryField;

/**
 * AHibernate概要 <br/>
 * (一)支持功能: 1.自动建表,支持属性来自继承类:可根据注解自动完成建表,并且对于继承类中的注解字段也支持自动建表. 2.自动支持增删改
 * ,增改支持对象化操作:增删改是数据库操作的最基本单元,不用重复写这些增删改的代码,并且添加和更新支持类似于hibernate中的对象化操作.
 * 3.查询方式灵活:支持android框架提供的方式,也支持原生sql方式.
 * 4.查询结果对象化:对于查询结果可自动包装为实体对象,类似于hibernate框架.
 * 5.查询结果灵活:查询结果支持对象化,也支持结果为List<Map<String,String>>形式,这个方法在实际项目中很实用,且效率更好些.
 * 6.日志较详细:因为android开发不支持热部署调试,运行报错时可根据日志来定位错误,这样可以减少运行Android的次数. <br/>
 * (二)不足之处: <br/>
 * 1.id暂时只支持int类型,不支持uuid,在sqlite中不建议用uuid.
 * 2.现在每个方法都自己开启和关闭事务,暂时还不支持在一个事务中做多个操作然后统一提交事务. <br/>
 * (三)作者寄语:<br/>
 * 昔日有JavaScript借Java发展,今日也希望AHibernate借Hibernate之名发展.
 * 希望这个项目以后会成为开源社区的重要一员,更希望这个项目能给所有Android开发者带便利.
 * 欢迎访问我的博客:http://blog.csdn.net/lk_blog,
 * 这里有这个框架的使用范例和源码,希望朋友们多多交流完善这个框架,共同推动中国开源事业的发展,AHibernate期待与您共创美好未来!!!
 */
public class BaseDaoImpl<T> implements BaseDao<T> {
	/**
	 * 应用包名
	 */
	public static final String PACKAGE_NAME = "com.sharpgrid.healthclient";

	/**
	 * 在手机里存放数据库的位置
	 */
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases/";

	/**
	 * 插入
	 */
	private static final int METHOD_INSERT = 0;

	/**
	 * 修改
	 */
	private static final int METHOD_UPDATE = 1;

	/**
	 * Id不自动递增
	 */
	private static final int TYPE_NOT_INCREMENT = 0;

	/**
	 * Id自动递增
	 */
	private static final int TYPE_INCREMENT = 1;

	/**
	 * 日志对象
	 */
	private static HealthLog log = HealthLog.getLog(BaseDaoImpl.class);

	// /**
	// * sqlite数据库打开帮助类
	// */
	// private static SQLiteOpenHelper dbHelper;

	// private SQLiteDatabase db;

	/**
	 * UI上下文
	 */
	private static Context context;

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * Id字段
	 */
	private String idColumn;

	/**
	 * 持久化对象类信息
	 */
	private Class<T> clazz;

	/**
	 * 持久化对象中所有字段
	 */
	private List<Field> allFields;

	// /**
	// * 数据库密码
	// */
	// private final String password = "healthgrid01";

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param clazz
	 *            持久化类信息
	 */
	public BaseDaoImpl(Context context, Class<T> clazz) {

		// if (null == dbHelper) {
		if (null == BaseDaoImpl.context) {
			BaseDaoImpl.context = context;
		}

		// 判断数据库文件是否存在，如果不存在，将初始的数据库文件copy过来。
		File dbPath = new File(DB_PATH);
		if (!dbPath.exists()) {
			dbPath.mkdirs(); // 如果目录不存在，则新建。
		}

		boolean dbFileExists = false;
		if (HealthUtils.isNotEmpty(dbPath.list())) {
			for (String fileName : dbPath.list()) {
				if (DBHelper.DB_NAME.equals(fileName)) {
					dbFileExists = true; // 存在数据库文件
				}
			}
		}

		if (!dbFileExists) {
			// 数据库文件不存在，将初始化的数据库文件copy过来。
			// 未加密
			copyDataFile();
			// 加密
			// File databaseFile = new File(DB_PATH + DBHelper.DB_NAME);
			// databaseFile.mkdirs();
			// databaseFile.delete();
			// SQLiteDatabase database =
			// SQLiteDatabase.openOrCreateDatabase(
			// databaseFile, password, null);

		}

		// dbHelper = new DBHelper(BaseDaoImpl.context);
		// }

		if (clazz == null) {
			// this.clazz = ((Class<T>) ((java.lang.reflect.ParameterizedType)
			// this.getClass().getGenericSuperclass())
			// .getActualTypeArguments()[0]);

			// ParameterizedType parameterizedType = (ParameterizedType)
			// super.getClass().getGenericSuperclass();
			// Type[] types = parameterizedType.getActualTypeArguments();
			// this.clazz = (Class<T>) types[0];

			// try
			// {
			// Method method = getClass().getMethod("update", new Class[0]);
			// System.out.println(method);
			// }
			// catch (NoSuchMethodException e)
			// {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		} else {
			this.clazz = clazz;
		}

		if (null != this.clazz) {
			setTableInfo();
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 */
	public BaseDaoImpl(Context context) {
		this(context, null);
	}

	// /** {@inheritDoc} */
	// @Override
	// public SQLiteOpenHelper getDbHelper() {
	// return dbHelper;
	// }

	/** {@inheritDoc} */
	@Override
	public T get(int id) {
		String selection = this.idColumn + " = ?";
		String[] selectionArgs = { Integer.toString(id) };

		log.debug("[get]: select * from ", this.tableName, " where ",
				this.idColumn, " = '", String.valueOf(id), "'");

		List<T> list = find(null, selection, selectionArgs, null, null, null,
				null);
		if ((list != null) && (list.size() > 0)) {
			return list.get(0);
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public List<T> rawQuery(String sql, String[] selectionArgs) {
		log.debug("[rawQuery]: ", getLogSql(sql, selectionArgs));

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			// db = dbHelper.getReadableDatabase(password);
			db = // DBHelper.getInstance(context, true);
			DBHelper.getInstanceReadable(context);
			// db = dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			log.error(e, "[rawQuery] from DB Exception.");
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			// if (db != null) {
			// db.close();
			// }
		}

		return list;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isExist(String sql, String[] selectionArgs) {
		log.debug("[isExist]: ", getLogSql(sql, selectionArgs));

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			// db = dbHelper.getReadableDatabase(password);
			db = // DBHelper.getInstance(context, true);
			DBHelper.getInstanceReadable(context);
			// db = dbHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, selectionArgs);
			if (cursor.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error(e, "[isExist] from DB Exception.");
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			// if (db != null) {
			// db.close();
			// }
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public List<T> find() {
		return find(null, null, null, null, null, null, null);
	}

	/** {@inheritDoc} */
	@Override
	public List<T> find(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		log.debug("[find]");

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			// db = dbHelper.getReadableDatabase(password);
			db = // DBHelper.getInstance(context, true);
			DBHelper.getInstanceReadable(context);
			// db = dbHelper.getReadableDatabase();
			cursor = db.query(this.tableName, columns, selection,
					selectionArgs, groupBy, having, orderBy, limit);

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			log.error(e, "[find] from DB Exception");
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			// if (db != null) {
			// db.close();
			// }
		}

		return list;
	}

	/**
	 * 将数据返回记录封装到持久化对象中
	 * 
	 * @param list
	 *            持久化对象集合
	 * @param cursor
	 *            数据库查询返回游标
	 * @throws IllegalAccessException
	 *             IllegalAccessException
	 * @throws InstantiationException
	 *             InstantiationException
	 */
	private void getListFromCursor(List<T> list, Cursor cursor)
			throws IllegalAccessException, InstantiationException {

		HashMap<Field, Integer> map = new HashMap<Field, Integer>();

		for (Field field : this.allFields) {
			Column column = null;
			if (field.isAnnotationPresent(Column.class)) {
				column = field.getAnnotation(Column.class);

				field.setAccessible(true);
				Class<?> fieldType = field.getType();

				int c = cursor.getColumnIndex(column.name());

				map.put(field, c);
			}
		}

		while (cursor.moveToNext()) {
			T entity = this.clazz.newInstance();

			for (Field field : this.allFields) {
				// Column column = null;
				// if (field.isAnnotationPresent(Column.class)) {
				// column = field.getAnnotation(Column.class);
				//
				// field.setAccessible(true);
				Class<?> fieldType = field.getType();
				//
				// int c = cursor.getColumnIndex(column.name());

				int c = map.get(field);

				if (c < 0) {
					continue; // 如果不存则循环下个属性值
				} else if ((Integer.TYPE == fieldType)
						|| (Integer.class == fieldType)) {
					field.set(entity, cursor.getInt(c));
				} else if (String.class == fieldType) {
					field.set(entity, cursor.getString(c));
				} else if ((Long.TYPE == fieldType)
						|| (Long.class == fieldType)) {
					field.set(entity, Long.valueOf(cursor.getLong(c)));
				} else if ((Float.TYPE == fieldType)
						|| (Float.class == fieldType)) {
					field.set(entity, Float.valueOf(cursor.getFloat(c)));
				} else if ((Short.TYPE == fieldType)
						|| (Short.class == fieldType)) {
					field.set(entity, Short.valueOf(cursor.getShort(c)));
				} else if ((Double.TYPE == fieldType)
						|| (Double.class == fieldType)) {
					field.set(entity, Double.valueOf(cursor.getDouble(c)));
				} else if (Date.class == fieldType) {
					// 处理java.util.Date类型,update2012-06-10
					Date date = new Date();
					date.setTime(cursor.getLong(c));
					field.set(entity, date);
				} else if (Blob.class == fieldType) {
					field.set(entity, cursor.getBlob(c));
				} else if (Character.TYPE == fieldType) {
					String fieldValue = cursor.getString(c);

					if ((fieldValue != null) && (fieldValue.length() > 0)) {
						field.set(entity,
								Character.valueOf(fieldValue.charAt(0)));
					}
				}
			}
			// }

			list.add(entity);
		}
	}

	/** {@inheritDoc} */
	@Override
	public long insert(T entity) {
		return insert(entity, true);
	}

	/** {@inheritDoc} */
	@Override
	public long bulkInsert(List<T> entity, boolean flag) {
		SQLiteDatabase db = null;
		Log.w("bulkInsert:", "begin");
		try {
			// 1.初始化数据库
			// db = dbHelper.getWritableDatabase(password);
			db = // DBHelper.getInstance(context, false);
			DBHelper.getInstancedWritable(context);
			// db = dbHelper.getWritableDatabase();
			// 2.开启事务
			db.beginTransaction();

			exeBulkInsert(entity, true, db);

			// 6.事务成功
			db.setTransactionSuccessful();
			Log.w("bulkInsert:", "Done");
		} catch (Exception e) {
			Log.w("bulkInsert:", e);
		} finally {
			// 7.事务结束
			db.endTransaction();
			// db.close();
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	public long exeBulkInsert(List<T> entity, boolean flag, SQLiteDatabase db) {
		try {
			Class c = entity.get(0).getClass();

			// 3.编写sql语句
			String sql = "insert into " + this.tableName + " (";
			StringBuffer strField = new StringBuffer();// 所有参数名称
			StringBuffer strValue = new StringBuffer(" values(");// 所有值

			int count = 0;
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			// 根据表名获得参数名称
			for (Field field : this.allFields) {
				if (!field.isAnnotationPresent(Column.class)) {
					continue;
				}
				Column column = field.getAnnotation(Column.class);
				strField.append(column.name()).append(',');
				strValue.append('?').append(',');

				count++;
				map.put(field.getName(), count);// columnNameToFieldName(column.name()),count);
				// System.out.println(columnNameToFieldName(column.name())+","+count);
			}
			strField.deleteCharAt(strField.length() - 1).append(')');
			strValue.deleteCharAt(strValue.length() - 1).append(')');
			sql = sql + strField.toString() + strValue.toString();
			System.out.println(sql);
			// 4.生产SQLiteStatement的实例
			SQLiteStatement insert = db.compileStatement(sql);

			// 5.批量插入数据

			// 1)通过反射获得方法名
			Method methlist[] = c.getDeclaredMethods();
			Method superMethlist[] = c.getSuperclass().getDeclaredMethods();
			List<String> methodName = new ArrayList<String>();
			List<String> superMethodName = new ArrayList<String>();

			int length = methlist.length / 2;
			int superLength = superMethlist.length / 2;

			// 根据获得的setMethodName截取获得MethodName
			for (int i = length; i < methlist.length; i++) {
				methodName.add(methodNameToFieldName(methlist[i].getName()));
				// System.out.println(methodNameToFieldName(methlist[i].getName()));
			}

			for (int i = 0; i < superLength; i++) {
				if (!superMethlist[i].getName().equals("getId")) {
					superMethodName.add(methodNameToFieldName(superMethlist[i]
							.getName()));
				}
			}

			for (T t : entity) {
				// 每条记录
				for (int i = 0; i < length; i++) {
					// 每列数据
					// 根据fieldName获得所有的数值

					Field fields = t.getClass().getDeclaredField(
							methodName.get(i));
					try {
						if (!fields.isAccessible())// 判断该对象是否可以访问
						{
							fields.setAccessible(true);
						}
						Class<?> fieldType = fields.getType();

						if (String.class == fieldType) {
							// System.out.println("key:"+fields.getName()+","+"value:"+map.get(fields.getName()));
							// System.out.println("值："+String.valueOf(fields.get(t)));
							if (fields.get(t) == null) {
								insert.bindString(map.get(fields.getName()), "");
							} else {
								insert.bindString(map.get(fields.getName()),
										String.valueOf(fields.get(t)));
							}
						} else if ((Integer.TYPE == fieldType)
								|| (Integer.class == fieldType)) {
							insert.bindLong(map.get(fields.getName()), Long
									.parseLong(String.valueOf(fields.get(t))));

						} else if ((Boolean.TYPE == fieldType)
								|| (Boolean.class == fieldType)) {

							// insert.bindLong(map.get(fields.getName()),
							// Long.parseLong(String.valueOf(fields.get(t))));
							// System.out.println("boolean");
						} else if (List.class == fieldType) {
						}

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				for (int i = 0; i < superLength - 1; i++) {
					// 每列数据
					// 根据fieldName获得所有的数值
					Field fields = t.getClass().getSuperclass()
							.getDeclaredField(superMethodName.get(i));
					try {
						if (!fields.isAccessible())// 判断该对象是否可以访问
						{
							fields.setAccessible(true);
						}

						insert.bindString(map.get(fields.getName()),
								String.valueOf(fields.get(t)));
					} catch (Exception e) {
					}
				}

				insert.executeInsert();
			}
		} catch (Exception e) {
			Log.w("bulkInsert:", e);
		}
		return 0;

	}

	public String methodNameToFieldName(String methodName) {
		String FieldName = methodName.substring(3);
		char[] chars = new char[1];
		chars[0] = FieldName.charAt(0);
		String temp = new String(chars);
		FieldName = FieldName.replaceFirst(temp, temp.toLowerCase());
		return FieldName;
	}

	// /*
	// * 将ACCEPT_WAY转化为acceptWay形式
	// * */
	// public String columnNameToFieldName(String columnName){
	// String fieldName="";
	// String[] columns=new String[]{};
	// if(columnName.contains("_"))
	// {
	//
	// columns=columnName.toLowerCase().split("_");
	// fieldName=columns[0];
	//
	// for(int i=1;i<columns.length;i++){
	// // if(columns[i].equals("ehr")){
	// // fieldName+="EHR";
	// // }else{
	// fieldName+=toUpperCaseFirstOne(columns[i]);
	// // }
	// }
	// }else{
	//
	// fieldName=columnName.toLowerCase();
	// }
	//
	//
	// return fieldName;
	//
	// }
	//
	// //首字母转大写
	// public static String toUpperCaseFirstOne(String s)
	// {
	// return (new
	// StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	// }

	/** {@inheritDoc} */
	@Override
	public long insert(T entity, boolean flag) {
		String sql = "";
		SQLiteDatabase db = null;
		try {
			// db = dbHelper.getWritableDatabase(password);
			db = // DBHelper.getInstance(context, false);
			DBHelper.getInstancedWritable(context);
			// db = dbHelper.getWritableDatabase();
			ContentValues cv = new ContentValues();
			if (flag) {
				sql = setContentValues(entity, cv, TYPE_INCREMENT,
						METHOD_INSERT);// id自增
			} else {
				sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
						METHOD_INSERT);// id需指定
			}

			log.debug("[insert]: insert into ", this.tableName, " ", sql);

			long row = db.insert(this.tableName, null, cv);

			return row;
		} catch (Exception e) {
			log.debug(e, "[insert] into DB Exception.");
		} finally {
			// if (db != null) {
			// db.close();
			// }
		}

		return 0L;
	}

	/** {@inheritDoc} */
	@Override
	public void delete(int id) {
		SQLiteDatabase db = // DBHelper.getInstance(context, false);
		// SQLiteDatabase db = dbHelper.getWritableDatabase(password);
		DBHelper.getInstancedWritable(context);
		String where = this.idColumn + " = ?";
		String[] whereValue = { Integer.toString(id) };

		log.debug("[delete]: delelte from ", this.tableName, " where ",
				where.replace("?", String.valueOf(id)));

		db.delete(this.tableName, where, whereValue);
		// db.close();
	}

	/** {@inheritDoc} */
	@Override
	public void delete(Integer... id) {
		if (id.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < id.length; i++) {
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			SQLiteDatabase db = // dbHelper.getWritableDatabase(password);
			// DBHelper.getInstance(context, false);
			DBHelper.getInstancedWritable(context);
			String sql = "delete from " + this.tableName + " where "
					+ this.idColumn + " in (" + sb + ")";

			log.debug("[delete]: ", getLogSql(sql, id));

			db.execSQL(sql, id);
			// db.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void deleteUser(String userId) {
		SQLiteDatabase db = // dbHelper.getWritableDatabase(password);
		// DBHelper.getInstance(context, false);
		DBHelper.getInstancedWritable(context);
		String sql = "delete from T_SG_USER_INDI where USER_ID =" + userId;
		db.execSQL(sql);
		sql = "delete from T_SG_USER_EHR where BATCH_ID IN (SELECT BATCH_ID FROM T_SG_USER_EHR_BATCH  WHERE USER_ID = "
				+ userId + ")";
		db.execSQL(sql);

		sql = "delete from T_SG_USER_EHR_BATCH where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_ADDRESS where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_CARD where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_COMMENTS where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_CONTACT where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_EHR_INDICATOR where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_FAVOR_INDI where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_MSG_REPLY where MSG_ID IN (SELECT MSG_ID FROM T_SG_MESSAGE  WHERE USER_ID = "
				+ userId + ")";
		db.execSQL(sql);

		sql = "delete from T_SG_MESSAGE where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_REMINDER where USER_ID =" + userId;
		db.execSQL(sql);

		sql = "delete from T_SG_USER where USER_ID =" + userId;
		db.execSQL(sql);

		// db.close();

	}

	/** {@inheritDoc} */
	@Override
	public void deleteByAppointCol(String column, String value) {
		SQLiteDatabase db = // DBHelper.getInstance(context, false);//
							// dbHelper.getWritableDatabase(password);
		DBHelper.getInstancedWritable(context);
		String where = column + " = ?";

		log.debug("[deleteByAppointCol]: delelte from ", this.tableName,
				" where ", where.replace("?", value));

		db.delete(this.tableName, where, new String[] { value });
		// db.close();
	}

	/** {@inheritDoc} */
	@Override
	public void deleteByAppointCol(String column, Object... values) {
		if (values.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			SQLiteDatabase db = // dbHelper.getWritableDatabase(password);
			// DBHelper.getInstance(context, false);
			DBHelper.getInstancedWritable(context);
			String sql = "delete from " + this.tableName + " where " + column
					+ " in (" + sb + ")";

			log.debug("[deleteByAppointCol]: ", getLogSql(sql, values));

			db.execSQL(sql, values);
			// db.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void update(T entity) {
		SQLiteDatabase db = null;
		try {
			db = // DBHelper.getInstance(context, false);//
					// dbHelper.getWritableDatabase(password);
			DBHelper.getInstancedWritable(context);
			ContentValues cv = new ContentValues();

			String sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
					METHOD_UPDATE);

			String where = this.idColumn + " = ?";
			int id = Integer.parseInt(cv.get(this.idColumn).toString());
			cv.remove(this.idColumn);

			log.debug("[update]: update ", this.tableName, " set ", sql,
					" where ", where.replace("?", String.valueOf(id)));

			String[] whereValue = { Integer.toString(id) };
			db.update(this.tableName, cv, where, whereValue);
		} catch (Exception e) {
			log.error(e, "[update] DB Exception.");
		} finally {
			// if (db != null)
			// db.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void update(T entity, int id) {
		SQLiteDatabase db = null;
		try {
			db = // DBHelper.getInstance(context, false);//
					// dbHelper.getWritableDatabase(password);
			DBHelper.getInstancedWritable(context);
			ContentValues cv = new ContentValues();
			cv.put(this.idColumn, id);
			String sql = setContentValues(entity, cv, TYPE_NOT_INCREMENT,
					METHOD_UPDATE);

			String where = this.idColumn + " = ?";
			// int id = Integer.parseInt(cv.get(this.idColumn).toString());
			cv.remove(this.idColumn);

			log.debug("[update]: update ", this.tableName, " set ", sql,
					" where ", where.replace("?", String.valueOf(id)));

			String[] whereValue = { Integer.toString(id) };
			db.update(this.tableName, cv, where, whereValue);
		} catch (Exception e) {
			log.error(e, "[update] DB Exception.");
		} finally {
			// if (db != null)
			// db.close();
		}
	}

	/**
	 * 添加或修改时设置字段值
	 * 
	 * @param entity
	 *            持久化对象
	 * @param cv
	 *            持久化对象中字段键值对
	 * @param type
	 *            Id产生类型
	 * @param method
	 *            操作类型
	 * @return 拼装好的sql
	 * @throws IllegalAccessException
	 *             IllegalAccessException
	 */
	private String setContentValues(T entity, ContentValues cv, int type,
			int method) throws IllegalAccessException {
		StringBuffer strField = new StringBuffer('(');
		StringBuffer strValue = new StringBuffer(" values(");
		StringBuffer strUpdate = new StringBuffer(' ');

		for (Field field : this.allFields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = field.getAnnotation(Column.class);

			field.setAccessible(true);
			Object fieldValue = field.get(entity);
			if (fieldValue == null) {
				continue;
			}

			if ((type == TYPE_INCREMENT)
					&& (field.isAnnotationPresent(Id.class))) {
				continue;
			}

			if (Date.class == field.getType()) {
				// 处理java.util.Date类型,update 2012-06-10
				cv.put(column.name(), ((Date) fieldValue).getTime());
				continue;
			}

			String value = String.valueOf(fieldValue);
			cv.put(column.name(), value);
			if (method == METHOD_INSERT) {
				strField.append(column.name()).append(',');
				strValue.append('\'').append(value).append("',");
			} else {
				strUpdate.append(column.name()).append('=').append('\'')
						.append(value).append("',");
			}

		}
		if (method == METHOD_INSERT) {
			strField.deleteCharAt(strField.length() - 1).append(')');
			strValue.deleteCharAt(strValue.length() - 1).append(')');

			return strField.toString() + strValue.toString();
		} else {
			return strUpdate.deleteCharAt(strUpdate.length() - 1).append(' ')
					.toString();
		}
	}

	/**
	 * 将查询的结果保存为名值对map.
	 * 
	 * @param sql
	 *            查询sql
	 * @param selectionArgs
	 *            参数值
	 * @return 返回的Map中的key全部是小写形式.
	 */
	@Override
	public List<Map<String, String>> query2MapList(String sql,
			String[] selectionArgs) {
		log.debug("[query2MapList]: " + getLogSql(sql, selectionArgs));

		SQLiteDatabase db = null;
		Cursor cursor = null;
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		try {
			db = // DBHelper.getInstance(context, true);//
					// dbHelper.getReadableDatabase(password);
			DBHelper.getInstanceReadable(context);
			cursor = db.rawQuery(sql, selectionArgs);
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String columnName : cursor.getColumnNames()) {
					int c = cursor.getColumnIndex(columnName);
					if (c < 0) {
						continue; // 如果不存在循环下个属性值
					} else {
						map.put(columnName.toLowerCase(Locale.getDefault()),
								cursor.getString(c));
					}
				}
				retList.add(map);
			}
		} catch (Exception e) {
			log.error(e, "[query2MapList] from DB exception");
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			// if (db != null) {
			// db.close();
			// }
		}

		return retList;
	}

	/**
	 * 封装执行sql代码.
	 * 
	 * @param sql
	 *            sql
	 * @param selectionArgs
	 *            selectionArgs
	 */
	@Override
	public void execSql(String sql, Object[] selectionArgs) {
		log.debug("[execSql]: ", getLogSql(sql, selectionArgs));

		SQLiteDatabase db = null;
		try {
			db = // DBHelper.getInstance(context, false);//
					// dbHelper.getWritableDatabase(password);
			DBHelper.getInstancedWritable(context);
			if (selectionArgs == null) {
				db.execSQL(sql);
			} else {
				db.execSQL(sql, selectionArgs);
			}
		} catch (Exception e) {
			log.error(e, "[execSql] DB exception.");
		} finally {
			// if (db != null) {
			// db.close();
			// }
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<T> query(Condition condition, boolean userStatus) {
		// 排除逻辑删除的数据
		if (userStatus) {
			condition.setQueryField(TBasePO.STATUS, DataStatus.DELETE,
					QueryField.NE);
		}

		StringBuilder selection = null;
		String[] selectionArgs = null;
		StringBuilder orderBy = null; // 排序

		Map<String, QueryField> queryFields = condition.getQueryFields();
		// where后面的查询条件
		if (HealthUtils.isNotEmpty(queryFields)) {
			selection = new StringBuilder(Num4Int.NUM_100); // " USER_ID = ? "
			selectionArgs = new String[queryFields.size()]; // ? 取代值
			int i = 0;
			boolean firstQueryField = true; // 标识是否是第一个查询条件
			for (Entry<String, QueryField> entry : queryFields.entrySet()) {
				if (firstQueryField) {
					firstQueryField = false;
				} else {
					selection.append(" and");
				}

				selection.append(' ').append(entry.getKey()).append(' ')
						.append(entry.getValue().getQueryType()).append(" ?");

				// 设置查询字段值
				if (QueryField.LIKE.equals(entry.getValue().getQueryType())) // 模糊查询
				{
					selectionArgs[i] = "%".concat(
							entry.getValue().getValue().toString()).concat("%");
				} else {
					selectionArgs[i] = entry.getValue().getValue().toString();
				}

				i++;
			}
		}

		// order by后面的排序条件
		List<OrderField> orderFields = condition.getOrderFields();
		if (HealthUtils.isNotEmpty(orderFields)) {
			orderBy = new StringBuilder(Num4Int.NUM_100); // " UPDATE_TIME desc "

			boolean firstOrderField = true; // 标识是否是第一个排序条件
			for (OrderField orderField : orderFields) {
				if (firstOrderField) {
					firstOrderField = false;
				} else {
					orderBy.append(" ,");
				}
				orderBy.append(' ').append(orderField.getField()).append(' ')
						.append(orderField.getOrderType());
			}
		}

		return find(null, null != selection ? selection.toString() : null,
				selectionArgs, null, null, null != orderBy ? orderBy.toString()
						: null, null);
	}

	/** {@inheritDoc} */
	@Override
	public List<T> search(String userId, String queryText, String msgGrid) {
		String sql = "";
		String[] selectionArgs = null;
		if (!msgGrid.equals("-1")) {
			sql = "SELECT * FROM T_SG_MESSAGE WHERE (TITLE LIKE ? AND USER_ID=? AND MSG_GRID=?) OR "
					+ "(CONTENT LIKE ? AND USER_ID=? AND MSG_GRID=?) OR (SENDER_INST_NAME LIKE ? AND USER_ID=? AND MSG_GRID=?)";
			selectionArgs = new String[] { "%" + queryText + "%", userId,
					msgGrid, "%" + queryText + "%", userId, msgGrid,
					"%" + queryText + "%", userId, msgGrid };
		} else {
			sql = "SELECT * FROM T_SG_MESSAGE WHERE (TITLE LIKE ? AND USER_ID=?) OR "
					+ "(CONTENT LIKE ? AND USER_ID=?) OR (SENDER_INST_NAME LIKE ? AND USER_ID=?)";
			selectionArgs = new String[] { "%" + queryText + "%", userId,
					"%" + queryText + "%", userId, "%" + queryText + "%",
					userId };
		}

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = // DBHelper.getInstance(context, true);//
					// dbHelper.getReadableDatabase(password);
			DBHelper.getInstanceReadable(context);
			cursor = db.rawQuery(sql, selectionArgs);
			log.debug("[rawQuery]: ", getLogSql(sql, selectionArgs));

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			log.error(e, "[rawQuery] from DB Exception.");
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			// if (db != null) {
			// db.close();
			// }
		}

		return list;

	}

	/** {@inheritDoc} */
	@Override
	public List<T> searchOneColumn(String queryText, String table, String column) {
		String sql = "";
		String[] selectionArgs = null;

		sql = "SELECT * FROM " + table + " WHERE " + column + " LIKE ?";
		selectionArgs = new String[] { "%" + queryText + "%" };

		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = // DBHelper.getInstance(context, true);//
					// dbHelper.getReadableDatabase(password);
			DBHelper.getInstanceReadable(context);
			cursor = db.rawQuery(sql, selectionArgs);
			log.debug("[rawQuery]: ", getLogSql(sql, selectionArgs));

			getListFromCursor(list, cursor);
		} catch (Exception e) {
			log.error(e, "[rawQuery] from DB Exception.");
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			// if (db != null) {
			// db.close();
			// }
		}

		return list;

	}

	/** {@inheritDoc} */
	@Override
	public String latestTimestamp(boolean byUserId) {
		// 记录最后一次更新时间戳
		String timestamp = null;

		StringBuilder sql = new StringBuilder(Num4Int.NUM_100);
		sql.append("select max(").append(TBasePO.UPDATE_TIME).append(')')
				.append(" as ").append(TBasePO.UPDATE_TIME);
		sql.append(" from ").append(this.tableName);

		List<Map<String, String>> list = null;
		if (byUserId) {
			sql.append(" where ").append(TSgUser.USER_ID).append(" =?");

			//TODO
			list = query2MapList(sql.toString(), new String[] {});
			// new String[] { SoapServer.getUserId() });
		} else {
			list = query2MapList(sql.toString(), null);
		}

		if (HealthUtils.isNotEmpty(list) && HealthUtils.isNotEmpty(list.get(0))) {
			for (Entry<String, String> entry : list.get(0).entrySet()) {
				timestamp = entry.getValue();
				break;
			}
		}

		if (null != timestamp) {
			return timestamp;
		} else {
			// 同步数据时如果本地找不到数据的时间戳，用此代替。
			return Constants.LONG_LONG_AGO;
		}
	}

	/**
	 * 返回 context
	 * 
	 * @return context
	 */
	protected Context getContext() {
		return context;
	}

	/**
	 * 拼装打印的日志
	 * 
	 * @param sql
	 *            sql
	 * @param args
	 *            args
	 * @return sql日志
	 */
	private String getLogSql(String sql, Object[] args) {
		if (args == null || args.length == 0) {
			return sql;
		}
		for (int i = 0; i < args.length; i++) {
			sql = sql.replaceFirst("\\?", "'" + String.valueOf(args[i]) + "'");
		}
		return sql;
	}

	/**
	 * 根据类信息设置数据库表信息
	 */
	private void setTableInfo() {
		if (this.clazz.isAnnotationPresent(Table.class)) {
			Table table = this.clazz.getAnnotation(Table.class);
			this.tableName = table.name();
		}

		// 加载所有字段
		this.allFields = TableHelper.joinFields(this.clazz.getDeclaredFields(),
				this.clazz.getSuperclass().getDeclaredFields());

		// 找到主键
		for (Field field : this.allFields) {
			if (field.isAnnotationPresent(Id.class)) {
				Column column = field.getAnnotation(Column.class);
				this.idColumn = column.name();
				break;
			}
		}

		log.debug("clazz:", this.clazz.toString(), " tableName:",
				this.tableName, " idColumn:", this.idColumn);
	}

	/**
	 * copy初始的数据库文件
	 */
	private void copyDataFile() {
		// Open your local db as the input stream
		InputStream initDBInput = null;
		// Open the empty db as the output stream
		OutputStream initDBOutput = null;
		try {
			initDBInput = BaseDaoImpl.context.getResources().getAssets()
					.open(DBHelper.DB_NAME);

			initDBOutput = new FileOutputStream(DB_PATH + DBHelper.DB_NAME);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[Num4Int.NUM_1024];
			int length = 0;
			while ((length = initDBInput.read(buffer)) > 0) {
				initDBOutput.write(buffer, 0, length);
			}

			initDBOutput.flush();
		} catch (IOException e) {
			log.error(e, "copyDataFile error:", e.getMessage());
		} finally {
			// Close the streams
			try {
				if (null != initDBOutput) {
					initDBOutput.close();
				}
				if (null != initDBInput) {
					initDBInput.close();
				}
			} catch (IOException e) {
				log.error(e, "copyDataFile close error:", e.getMessage());
			}
		}
	}

	@Override
	public Cursor search(String inputText) {
		// TODO Auto-generated method stub
		// SQLiteDatabase db = dbHelper.getWritableDatabase(password);
		// String query = "SELECT " + "TITLE(*)" + " FROM " + this.tableName +
		// " WHERE 内容" + " LIKE '" + "%" + inputText
		// + "%" + "';";
		// Cursor mCursor = db.rawQuery(query, null);
		//
		// if (mCursor != null)
		// {
		// mCursor.moveToFirst();
		// }
		return null;
	}
}