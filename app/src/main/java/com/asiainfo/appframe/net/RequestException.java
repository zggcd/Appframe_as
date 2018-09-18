package com.asiainfo.appframe.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;

@SuppressWarnings("deprecation")
public class RequestException extends Exception
{

	/**
	 * 异常状态码
	 */
	private int code = -1;

	/**
	 * 异常信息
	 */
	private String msg = null;

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	private static final long serialVersionUID = 1L;
	/**
	 * {@link EOFException} 抛出此类异常，表示连接丢失，也就是说网络连接的另一端非正常关闭连接（可能是主机断电、网线出现故障等导致）
	 */
	public final static int SERVER_CLOSED_EXCEPTION = 0x01;

	/**
	 * {@link SocketException} 抛出此类异常，表示无法连接，也就是说当前主机不存在
	 */
	public final static int CONNECT_EXCEPTION = 0X02;

	/**
	 * {@link SocketException} 抛出此类异常，表示
	 * <ul>
	 * <li>1、连接正常关闭，也就是说另一端主动关闭连接</li>
	 * <li>2、表示一端关闭连接，而另一端此时在读数据</li>
	 * <li>3、表示一端关闭连接，而另一端此时在发送数据</li>
	 * <li>4、表示连接已关闭，但还继续使用（也就是读/写操作）此连接</li>
	 * </ul>
	 */
	public final static int SOCKET_EXCEPTION = 0x03;

	/**
	 * {@link BindException} 抛出此类异常，表示端口已经被占用
	 */
	public final static int BIND_EXCEPTION = 0x04;

	/**
	 * {@link ConnectTimeoutException} 连接超时
	 */
	public final static int CONNECT_TIMEOUT_EXCEPTION = 0x05;

	/**
	 * {@link UnsupportedEncodingException} 不支持的编码异常
	 */
	public final static int UNSUPPORTED_ENCODEING_EXCEPTION = 0x06;

	/**
	 * {@link SocketTimeoutException} socket 超时异常
	 */
	public final static int SOCKET_TIMEOUT_EXCEPTION = 0x06;

	/**
	 * {@link ClientProtocolException} 客户端协议异常
	 */
	public final static int CLIENT_PROTOL_EXCEPTION = 0x07;

	/**
	 * {@link IOException} 读取异常
	 */
	public final static int IO_EXCEPTION = 0x08;

	/**
	 * {@link IOException} http状态码异常
	 */
	public final static int HTTP_STATUS_EXCEPTION = 0x09;

	/**
	 * {@link IOException} 网络不可用
	 */
	public final static int NET_INAVAILABLE_EXCEPTION = 0x10;

	public RequestException(int code, String msg, Throwable throwable)
	{
		super(msg, throwable);
		this.code = code;
		this.msg = msg;
		// TODO Auto-generated constructor stub
	}

	public RequestException(int code, Throwable throwable)
	{
		super(throwable);
		this.code = code;
	}

	public RequestException(int code, String msg)
	{
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public RequestException(String msg)
	{
		super(msg);
		this.msg = msg;
	}
	
	public RequestException(){};
}
