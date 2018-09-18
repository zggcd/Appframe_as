package com.asiainfo.appframe.exception;

/******************************************************************
 * 文件名称	: NotEnouchSpaceException.java
 * 作    者	: haiting.wang
 * 文件描述	: 手机空间内存和SD卡剩余空间均不足的异常
 ******************************************************************/
@SuppressWarnings("serial")
public class NotEnouchSpaceException extends Exception
{
	private String msg = null;
	
	/**
	 * 构造方法
	 * @param msg  异常信息   
	 */
	public NotEnouchSpaceException(String msg)
	{
		this.msg = msg;
	}

	/**
	 * 重写的方法，获取异常信息
	 * @return    异常信息
	 */
	@Override
	public String getMessage()
	{
		return msg;
	}
	
	
}
