/*
 * 文 件  名:  TableColumn.java
 * 版       权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2014,  All rights reserved
 * 描       述:   <描述>
 * 修 改  人:   潘凌越
 * 修改时间:  2014-7-28 下午06:23:23
 * 版 本   号:  Version 2.1.0 
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate.constants;


/**
 * 表字段常量定义
 */
public final class TableColumn {
	/**
	 * 所有持久化对象基类
	 */
	public static final class TBasePO {
		/**
		 * 数据修改时间：取系统当前时间(yyyyMMddHHmmss)
		 */
		public static final String UPDATE_TIME = "UPDATE_TIME";

		/**
		 * 数据状态
		 */
		public static final String STATUS = "STATUS";

		/**
		 * 数据id
		 */
		public static final String id = "id";

		/**
		 * 数据USER_ID
		 */
		public static final String USER_ID = "USER_ID";
	}

	/**
	 * 用户表
	 */
	public static final class TSgUser {
		/**
		 * ID
		 */
		public static final String ID = "id";
		/**
		 * 用户ID
		 */
		public static final String USER_ID = "USER_ID";

	}

	/**
	 * 消息表
	 */
	public static final class TSgMessage {
		/**
		 * 用户ID
		 */
		public static final String USER_ID = "USER_ID";

		/**
		 * 消息ID
		 */
		public static final String MSG_ID = "MSG_ID";

		/**
		 * 消息格子
		 */
		public static final String MSG_GRID = "MSG_GRID";

		/**
		 * 消息标题
		 */
		public static final String TITLE = "TITLE";

		/**
		 * 消息标题
		 */
		public static final String CONTENT = "CONTENT";

		/**
		 * 消息来源
		 */
		public static final String MSG_SOURCE = "MSG_SOURCE";

		/**
		 * 机构名称SENDER_INST_NAME
		 */
		public static final String SENDER_INST_NAME = "SENDER_INST_NAME";

		/**
		 * 通知时间REMIND_DATE
		 */
		public static final String REMIND_DATE = "REMIND_DATE";

		/**
		 * 创建时间CREATE_TIME
		 */
		public static final String CREATE_TIME = "CREATE_TIME";

		/**
		 * 是否已读0 未读 1 已读
		 */
		public static final String MSG_STATUS = "MSG_STATUS";

		/**
		 * 回复状态REPLY_STATUS 0 不需要 1 未回复 2 已回复
		 */
		public static final String REPLY_STATUS = "REPLY_STATUS";

	}

	/**
	 * 消息回复表
	 */
	public static final class TSgMsgReply {
		/**
		 * 消息回复ID
		 */
		public static final String MSG_REPLY_ID = "MSG_REPLY_ID";
	}

	/**
	 * 预约挂号表
	 */
	public static final class TSgRegisters {
		/**
		 * 预约挂号ID
		 */
		public static final String REG_ID = "REG_ID";

		/**
		 * 预约挂号预约时间
		 */
		public static final String VISIT_TIME = "VISIT_TIME";

		/**
		 * 预约挂号创建时间
		 */
		public static final String CREATE_TIME = "CREATE_TIME";

		/**
		 * 就诊状态 1 未就诊 2 已就诊 3 已过期
		 */
		public static final String SEE_STATUS = "SEE_STATUS";

		/**
		 * 是否支付 0是未支付 1是支付
		 */
		public static final String IS_PAY = "IS_PAY";
	}

	/**
	 * EHR批次表
	 */
	public static final class TSgBatch {
		/**
		 * EHR批次ID
		 */
		public static final String BATCH_ID = "BATCH_ID";

		/**
		 * 批次机构ID
		 */
		public static final String INST_ID = "INST_ID";

		/**
		 * 最新EHR时间
		 */
		public static final String LATEST_EHR = "LATEST_EHR";
	}

	/**
	 * EHR表
	 */
	public static final class TSgEHR {
		/**
		 * EHR的ID
		 */
		public static final String USER_EHR_ID = "USER_EHR_ID";

		/**
		 * EHR的BATCH_ID
		 */
		public static final String BATCH_ID = "BATCH_ID";

		/**
		 * EHR的ANALYSIS_DATE
		 */
		public static final String ANALYSIS_DATE = "ANALYSIS_DATE";

		/**
		 * EHR的EHR_STATUS
		 */
		public static final String EHR_STATUS = "EHR_STATUS";

	}

	/**
	 * 趋势表
	 */
	public static final class TSgTrend {
		/**
		 * 趋势ID
		 */
		public static final String INDI_ID = "INDI_ID";

		/**
		 * 更新时间
		 */
		public static final String ACQUISITION_TIME = "ACQUISITION_TIME";

		/**
		 * 更新时间
		 */
		public static final String id = "id";

		/**
		 * 标准名称
		 */
		public static final String STAND_NAME = "STAND_NAME";
	}

	// /**
	// * 标准指标表
	// */
	// public static final class TSgIndiStandard
	// {
	// /**
	// * 标准指标ID
	// */
	// public static final String STAND_ID = "STAND_ID";
	//
	// /**
	// * 标准指标名称
	// */
	// public static final String STAND_NAME = "STAND_NAME";
	//
	// /**
	// * 标准指标别名
	// */
	// public static final String NAME_ALIAS = "NAME_ALIAS";
	//
	// /**
	// * 标准指标是否可录入
	// */
	// public static final String CAN_INPUT = "CAN_INPUT";
	//
	// }

	/**
	 * EHR评论表
	 */
	public static final class TSgComment {
		/**
		 * EHR评论ID
		 */
		public static final String COMM_ID = "COMM_ID";

		/**
		 * EHR评论EHRID
		 */
		public static final String EHR_ID = "EHR_ID";
	}

	/**
	 * 收藏指标表
	 */
	public static final class TSgFavorIndi {
		/**
		 * 收藏FAVOR_INDI_ID
		 */
		public static final String FAVOR_INDI_ID = "FAVOR_INDI_ID";

		/**
		 * 指标名称INDI_NAME
		 */
		public static final String INDI_NAME = "INDI_NAME";

		/**
		 * 收藏ID
		 */
		public static final String id = "id";

		/**
		 * 更新时间
		 */
		public static final String UPDATE_TIME = "UPDATE_TIME";

		/**
		 * 用户USER_ID
		 */
		public static final String USER_ID = "USER_ID";

	}

	/**
	 * 模板表
	 */
	public static final class TSgTemp {
		/**
		 * 模板ID
		 */
		public static final String TEMP_ID = "TEMP_ID";
	}

	/**
	 * 基础数据表
	 */
	public static final class TSgCode {
		/**
		 * 基础数据ID
		 */
		public static final String CODE_ID = "CODE_ID";

		/**
		 * 类型编码TYPE_CODE
		 */
		public static final String TYPE_CODE = "TYPE_CODE";

		/**
		 * 编码数值CODE_VALUE
		 */
		public static final String CODE_VALUE = "CODE_VALUE";

		/**
		 * 编码名称CODE_NAME
		 */
		public static final String CODE_NAME = "CODE_NAME";
	}

	/**
	 * 日程提醒数据表
	 */
	public static final class TSgReminder {
		/**
		 * 日程提醒数据ID
		 */
		public static final String REMIND_ID = "REMIND_ID";

		/**
		 * 消息提醒数据MSG_ID
		 */
		public static final String MSG_ID = "MSG_ID";
	}

	/**
	 * 自定义机构数据表
	 */
	public static final class TCustInst {
		/**
		 * 表名
		 */
		public static final String T_CU_INST = "T_CU_INST";
		/**
		 * 机构ID
		 */
		public static final String INST_ID = "INST_ID";

		/**
		 * 机构名称
		 */
		public static final String INST_NAME = "INST_NAME";
	}

	/**
	 * 自定义用户数据表
	 */
	public static final class TCustUser {

		/**
		 * 表名
		 */
		public static final String T_CU_USER = "T_CU_USER";

		/**
		 * 用户账号
		 */
		public static final String USER_ACCOUNT = "USER_ACCOUNT";

		/**
		 * 用户密码
		 */
		public static final String USER_PASSWORD = "USER_PASSWORD";

		/**
		 * 用户ID
		 */
		public static final String USER_ID = "USER_ID";

		/**
		 * 是否记住密码
		 */
		public static final String IS_REMEMBER = "IS_REMEMBER";

		/**
		 * 登录时间
		 */
		public static final String LOGIN_TIME = "LOGIN_TIME";
	}

	/**
	 * 联系方式表
	 */
	public static final class TSgContact {
		/**
		 * 用户ID
		 */
		public static final String USER_ID = "USER_ID";
	}

	/**
	 * 身份证表
	 */
	public static final class TSgCard {
		/**
		 * 用户ID
		 */
		public static final String USER_ID = "USER_ID";

		/**
		 * 证件类型
		 */
		public static final String CARD_ZJLX = "CARD_ZJLX";
	}

	/**
	 * 住址表
	 */
	public static final class TSgAddress {
		/**
		 * 用户ID
		 */
		public static final String USER_ID = "USER_ID";
	}

	/**
	 * 日程提醒表
	 */
	public static final class TSgSchedule {
		/**
		 * 用户ID
		 */
		public static final String USER_ID = "USER_ID";

		/**
		 * 更新时间
		 */
		public static final String UPDATE_TIME = "UPDATE_TIME";

		/**
		 * 提醒时间
		 */
		public static final String REMIND_TIME = "REMIND_TIME";

	}
}