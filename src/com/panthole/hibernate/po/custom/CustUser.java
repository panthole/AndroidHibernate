/*
 * 文 件 名:  CustUser.java
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
import com.panthole.hibernate.ahibernate.annotation.Table;
import com.panthole.hibernate.po.BasePO;

/**
 * 用户账户持久化对象
 */
@Table(name = "T_CU_USER")
public class CustUser extends BasePO {
	/**
	 * 用户账号
	 */
	@Column(name = "USER_ACCOUNT")
	private String userAccount;

	/**
	 * 用户密码
	 */
	@Column(name = "USER_PASSWORD")
	private String userPassword;

	/**
	 * 用户ID
	 */
	@Column(name = "USER_ID")
	private String userId;

	/**
	 * 是否记住密码
	 */
	@Column(name = "IS_REMEMBER")
	private String isRemember;

	/**
	 * 用户头像
	 */
	@Column(name = "USER_PORTRAIT")
	private String userPortrait;

	/**
	 * 是否为最后一个登录
	 */
	@Column(name = "LOGIN_TIME")
	private String loginTime;

	/**
	 * @return the userPortrait
	 */
	public String getUserPortrait() {
		return userPortrait;
	}

	/**
	 * @param userPortrait
	 *            the userPortrait to set
	 */
	public void setUserPortrait(String userPortrait) {
		this.userPortrait = userPortrait;
	}

	/**
	 * @return the userAccount
	 */
	public String getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userAccount
	 *            the userAccount to set
	 */
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword
	 *            the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the isRemember
	 */
	public String getIsRemember() {
		return isRemember;
	}

	/**
	 * @param isRemember
	 *            the isRemember to set
	 */
	public void setIsRemember(String isRemember) {
		this.isRemember = isRemember;
	}

	/**
	 * @return the loginTime
	 */
	public String getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime
	 *            the loginTime to set
	 */
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

}