package cc.chenghong.huaxin.response;

import java.io.Serializable;

/**
 * 返回基类
 * @author hcl 2015 12 16
 *
 */
public class BaseResponse  implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final int CODE_SUCCESS = 200;
	/**
	 * 返回码 200:成功 300或其他: 失败
	 */
	public int code;
	/** 信息 */
	private String message;
	/**
	 * 总数
	 */
	public int total;
	/**
	 * 支付状态
	 */
	public int state;
	/**
	 * 返回码是否为200
	 * @return true为成功,false为失败
	 */
	public boolean isSuccess(){
		return code == CODE_SUCCESS;
	}
	
	public String getMessage(){
		return message == null ? "" : message;
	}
}
