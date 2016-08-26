package cc.chenghong.huaxin.response;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
  * 基类 封装http请求时提交的参数<br />
 * Service中请求网络时，可以将参数封装到这里，也可以单独写
 * @author JiaYe 2014年7月18日
 *
 */
public class EntityPost implements Serializable{
    private static final long serialVersionUID = 2895146521702870678L;
    /**
     * 转换成map供Http使用
     * @return
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> toMap(){
        Gson g = new GsonBuilder()
        //String类型如果为空的话，默认转换成空字符串
        /*.registerTypeAdapter(String.class, new TypeAdapter<String>() {
			@Override
			public String read(JsonReader reader) throws IOException {
				System.out.println("typeAdapter read>");
				return reader.nextString();
			}
			@Override
			public void write(JsonWriter writer, String str)
					throws IOException {
				System.out.println("typeAdapter write>"+str);
				writer.value(str==null?"":str);
			}
		})*/.create();
        return g.fromJson(g.toJson(this), HashMap.class);
    }
}
