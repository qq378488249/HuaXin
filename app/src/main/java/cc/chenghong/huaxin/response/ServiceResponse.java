package cc.chenghong.huaxin.response;



/**
 * HTTP请求返回的实体类
 * @author planet
 *
 */
public class ServiceResponse<T> {
	/**
	 * 数据
	 */
	public T data;
	public String errorMsg = "";
	/**
	 * 返回码
	 */
	public int responseCode;
	public boolean isDataValid(){
		return null != data;
	}

	/**
	 * 如果data继承自BaseResponse，判断是否服务器返回code=200
	 * @return
	 */
	public boolean isResponseSuccess(){
		if(data!=null && data instanceof BaseResponse){
			BaseResponse br = (BaseResponse)data;
			return br.isSuccess();
		}
		return false;
	}

	/**
	 * 尝试读取BaseResponse.data中的message
	 * @return
	 */
	public String getMessage(){
		if(data instanceof BaseResponse && ((BaseResponse)data).getMessage().length()>0){
			return ((BaseResponse)data).getMessage();
		}
		return errorMsg==null?"": errorMsg;
	}
	/**
	 * 尝试读取BaseResponse.data中的message
	 * @param prefix 前缀
	 * @return
	 */
	public String getMessage(String prefix){
		return prefix+getMessage();
	}
}
