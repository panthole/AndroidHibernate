/*
 * 文 件 名:  CustInst.java
 * 版    权:  SharpGrid Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  panly
 * 修改时间:  2014-7-24
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.panthole.hibernate.po.custom;

import com.panthole.hibernate.ahibernate.annotation.Column;
import com.panthole.hibernate.ahibernate.annotation.Id;
import com.panthole.hibernate.ahibernate.annotation.Table;
import com.panthole.hibernate.po.BasePO;
import com.panthole.hibernate.po.HealthPO;

/**
 * 医院名称持久化对象
 */
@Table(name = "T_CU_INST")
public class CustInst extends BasePO {
	/**
	 * 机构名
	 */
	@Column(name = "INST_NAME")
	private String instName;

	/**
	 * 机构ID
	 */
	@Column(name = "INST_ID")
	private String instID;

	/**
	 * @return the instName
	 */
	public String getInstName() {
		return instName;
	}

	/**
	 * @param instName
	 *            the instName to set
	 */
	public void setInstName(String instName) {
		this.instName = instName;
	}

	/**
	 * @return the instID
	 */
	public String getInstID() {
		return instID;
	}

	/**
	 * @param instID
	 *            the instID to set
	 */
	public void setInstID(String instID) {
		this.instID = instID;
	}

}