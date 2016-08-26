package cc.chenghong.huaxin.response;

import java.io.Serializable;
import java.util.List;

/**
 * 列表返回数据
 * @author hcl 2015 12 16
 *
 */
public class ListResponse<T> extends BaseResponse{
	/**
	 * 列表数据
	 */
	public List<T> data;
}
