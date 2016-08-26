package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Shop;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.request.ResponseHandler;

/**
 * 选择门店20160311
 */
public class SelectShopActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;

    CommonAdapter<Shop> adapter;
    List<Shop> list = new ArrayList<Shop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_select_shop);
        initXutils();
        setTitleName("选择门店");
        getData();
    }

    private void initLv() {
        adapter = new CommonAdapter<Shop>(this, list, R.layout.lv_item_shop) {
            @Override
            public void convert(ViewHolder helper, Shop item, int position) {
                helper.setText(R.id.tv1, item.getName());
                helper.setText(R.id.tv2, item.getContent());
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectShopActivity.this, LoginActivity.class).putExtra("data", list.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void getData() {
        progress("加载中...");
        AsyncHttpRequest.post(Api.organization_all, new ResponseHandler<Shop>() {

            @Override
            public void onSuccess(int var1, Shop data,Header[] headers) {
                hideProgress();
                if (data.isSuccess()){
                    list.addAll(data.data);
                    initLv();
                }
//                toask(data.data.size());
//                System.out.println(data.toString());
            }

            @Override
            public void onFailure(int var1, Header[] var2, String var3, Throwable var4) {
                no_network(var4);
            }
        });
//        AsyncHttpRequest.post(Api.organization_all, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                hideProgress();
//                if (i == 200) {
//                    Shop shop = fromJson(bytes, Shop.class);
//                    log_i(shop.data.size());
//                    list.addAll(shop.data);
//                    initLv();
//                } else {
//                    load_fail();
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                no_network(throwable);
//            }
//        });
//        BaseRequest.post(Api.organization_all, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                hideProgress();
////                ListResponse<Shop>
//                Shop shop = fromJson(s, Shop.class);
//                log_i(shop.data.size());
//                list.addAll(shop.data);
//                initLv();
////                list.addAll(suggest0ListResponse.data);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                hideProgress();
//                App.toask("加载失败");
//            }
//        });
    }
}
