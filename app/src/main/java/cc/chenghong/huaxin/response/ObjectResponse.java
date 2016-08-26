package cc.chenghong.huaxin.response;


/**
 * 对象返回数据
 * @author hcl 2015 12 16
 */
public class ObjectResponse<T> extends BaseResponse{
	private static final long serialVersionUID = -8115794317751297383L;
	/**
	 * 字符串类型的数据
	 */
	public T data;
}
