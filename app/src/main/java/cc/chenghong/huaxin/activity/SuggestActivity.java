package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Jkjy;
import cc.chenghong.huaxin.entity.Suggest_item;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;

/**
 * 健康建议 thin blue 20160224
 */

public class SuggestActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;
    private CommonAdapter<Suggest_item> adapter;
    private List<Suggest_item> list = new ArrayList<Suggest_item>();
    private Suggest_item s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_suggest);
        initXutils();
//        UITools.elasticListView(lv, 150);
        setTitleName("健康建议");
        s = new Suggest_item(R.drawable.a_0026_,"健康预警");
        list.add(s);
        s = new Suggest_item(R.drawable.a_0025_breads,"营养方案");
        list.add(s);
        s = new Suggest_item(R.drawable.a_0024_,"促销信息");
        list.add(s);
        s = new Suggest_item(R.drawable.a_0023_,"运动建议");
        list.add(s);
        s = new Suggest_item(R.drawable.a_0022_,"健康贴士");
        list.add(s);
        s = new Suggest_item(R.drawable.a_0021_,"服药提醒");
        list.add(s);
        initAdapter();
        getData(true);
    }

    private void getData(final boolean b) {
        if (b) load();
        AsyncHttpParams asyncHttpParams = AsyncHttpParams.New();
        asyncHttpParams.put("user_id", App.getUser().getId());
        AsyncHttpRequest.post(Api.info_jkjy_count, asyncHttpParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    Jkjy data = fromJson(bytes, Jkjy.class);
                    if (data.data != null) {
                        list.get(0).setCount(data.data.getJkyj_count());
                        list.get(1).setCount(data.data.getYyfa_count());
                        list.get(2).setCount(data.data.getCxxx_count());
                        list.get(3).setCount(data.data.getYdjy_count());
                        list.get(4).setCount(data.data.getJkts_count());
                        list.get(5).setCount(data.data.getFytx_count());
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    load_fail();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    //
    private void initAdapter() {
        adapter = new CommonAdapter<Suggest_item>(SuggestActivity.this,list,R.layout.lv_item_suggest) {
            @Override
            public void convert(ViewHolder helper, Suggest_item item, int position) {
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setImageResource(R.id.iv, item.getId());
                TextView tv = helper.getView(R.id.tv_count);
                ImageView iv = helper.getView(R.id.iv_rigth);
                if (item.getCount()!=null){
                    tv.setText(item.getCount());
                    if (item.getCount().equals("0")){
                        tv.setVisibility(View.GONE);
                        iv.setVisibility(View.VISIBLE);
                    }else {
                        iv.setVisibility(View.GONE);
                        tv.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        log_i(lv);
//        lv.addView(new TextView(this));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        openActivity(NewActivity.class,3);
//                        startActivity(new Intent(SuggestActivity.this,Suggest0Activity.class).putExtra("code" ,position));
                        break;
                    case 1:
                        startActivity(new Intent(SuggestActivity.this, Suggest1Activity.class).putExtra("code", position));
                        break;
                    case 2:
                        startActivity(new Intent(SuggestActivity.this, Suggest2Activity.class));
                        break;
                    case 3:
                        startActivity(new Intent(SuggestActivity.this, Suggest1Activity.class).putExtra("code", position));
                        break;
                    case 4:
                        startActivity(new Intent(SuggestActivity.this, Suggest1Activity.class).putExtra("code", position));
                        break;
                    case 5:
                        openActivity(Suggest5Activity.class);
                        break;
                }
            }
        });
    }
}
