package com.panthole.hibernate.constants;

/**
 * 常量定义
 */
public final class Constants {

	/**
	 * 成功
	 */
	public static final String SUCCESS = "0";

	/**
	 * 登录超时
	 */
	public static final String LOGOUTTIME = "31000003";

	/**
	 * 其他终端登录
	 */
	public static final String LOGOUT_BY_OTHER_DEVICE = "31000013";

	/**
	 * 用户不存在
	 */
	public static final String USERINEXIST = "31001003";

	/**
	 * 接口版本号不一致
	 */
	public static final String INTERFACE_VERSION_ERROR = "31000008";

	/**
	 * 排队信息为空
	 */
	public static final String QUEUEISEMPTY = "31009001";

	/**
	 * 没有队列信息需要更新
	 */
	public static final String NOQUEUETOUPDATE = "31009003";

	/**
	 * 手机已注册
	 */
	public static final String MOBILE_REGISTERED = "31001023";

	/**
	 * 预约挂号信息等待提交医院
	 */
	public static final String REGINFO_WAIT_TO_HOSPITAL = "31012010";

	/**
	 * 服务器异常
	 */
	public static final String SERVER_ECEPTION = "-1";

	/**
	 * 同步数据时如果本地找不到数据的时间戳，用此代替。
	 */
	public static final String LONG_LONG_AGO = "19491001000000";

	/**
	 * 同步数据时如果本地找不到数据的时间戳，用此代替。
	 */
	public static final String LONG_LONG_EVER = "21001001000000";

	/**
	 * INT型常量
	 */
	public static final class Num4Int {
		/**
		 * 数字2
		 */
		public static final int NUM_2 = 2;

		/**
		 * 数字3
		 */
		public static final int NUM_3 = 3;

		/**
		 * 数字4
		 */
		public static final int NUM_4 = 4;

		/**
		 * 数字8
		 */
		public static final int NUM_8 = 8;

		/**
		 * 数字14
		 */
		public static final int NUM_14 = 14;

		/**
		 * 数字20
		 */
		public static final int NUM_20 = 20;

		/**
		 * 数字24
		 */
		public static final int NUM_24 = 24;

		/**
		 * 数字100
		 */
		public static final int NUM_100 = 100;

		/**
		 * 数字110
		 */
		public static final int NUM_110 = 110;

		/**
		 * 数字1000
		 */
		public static final int NUM_1000 = 1000;

		/**
		 * 数字1024
		 */
		public static final int NUM_1024 = 1024;
	}

	/**
	 * 数据状态
	 */
	public static final class DataStatus {
		/**
		 * 新增
		 */
		public static final String INSERT = "0";

		/**
		 * 修改
		 */
		public static final String UPDATE = "1";

		/**
		 * 删除
		 */
		public static final String DELETE = "9";
	}

	/**
	 * 十六进制常量定义
	 */
	public static final class Hex {
		/**
		 * 0xf
		 */
		public static final byte HEX_0XF = 0xf;
	}

}