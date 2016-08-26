package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Test;
import cc.chenghong.huaxin.entity.Xycl;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;

/**
 * 健康追踪
 */
public class TraceActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;

    CommonAdapter<Test> adapter;
    List<Test> list = new ArrayList<Test>();

    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_trace);
        initXutils();
        setTitleName("健康追踪");
        initLv();
        getData(0, true);
    }

    private void getData(final int index, final boolean b) {
        String url = "";
        switch (index) {
            case 0:
                url = Api.xy_get_new;
                break;
            case 1:
                url = Api.xt_get_new;
                break;
            case 2:
                url = Api.tz_get_new;
                break;
        }
        if (b) load();
        AsyncHttpParams asyncHttpParams = AsyncHttpParams.New();
        asyncHttpParams.put("user_id", App.getUser().getId());
        AsyncHttpRequest.post(url, asyncHttpParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    Xycl data = fromJson(bytes, Xycl.class);
                    data = data.data;
                    switch (index) {
                        case 0:
                            if (data == null) {
//                                if (index < 2) {
//                                    getData(index + 1, b);
//                                } else {
//                                    if (adapter == null) initLv();
//                                    adapter.notifyDataSetChanged();
//                                }
//                                return;
                                list.get(index).setStr2("暂无数据");
                            }else{
                                list.get(index).setStr2(data.getSsy() + "/" + data.getSzy() + " mmHg " + data.getXl() + " bmp");
                            }
                            break;
                        case 1:
                            if (data == null) {
//                                if (index < 2) {
//                                    getData(index + 1, b);
//                                } else {
//                                    if (adapter == null) initLv();
//                                    adapter.notifyDataSetChanged();
//                                }
//                                return;
                                list.get(index).setStr2("暂无数据");
                            }else{
                                list.get(index).setStr2(data.getXt() + " mmol/L");
                            }
                            break;
                        case 2:
                            if (data == null) {
//                                if (index < 2) {
//                                    getData(index + 1, b);
//                                } else {
//                                    if (adapter == null) initLv();
//                                    adapter.notifyDataSetChanged();
//                                }
//                                return;
                                list.get(index).setStr2("暂无数据");
                            }else{
                                list.get(index).setStr2(data.getTz() + " KG");
                            }
                            break;
                    }
                    if (index < 2) {
                        getData(index + 1, b);
                    } else {
                        if (adapter == null) initLv();
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    load_fail();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });

    }

    private void initLv() {
        Test test;
        test = new Test(1, R.drawable.a_0029_, "血压", "");
        list.add(test);
        test = new Test(2, R.drawable.a_0028_, "血糖", "");
        list.add(test);
        test = new Test(3, R.drawable.a_0027_, "体脂", "");
        list.add(test);

        adapter = new CommonAdapter<Test>(this, list, R.layout.lv_item_trace) {
            @Override
            public void convert(ViewHolder helper, Test item, int position) {
                helper.setText(R.id.tv_1, item.getStr());
                helper.setText(R.id.tv_2, item.getStr2());
                helper.setImageResource(R.id.iv, item.getIv_id());
            }
        };

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int code = position + 1;
                openActivity(HistoryActivity.class, code);
            }
        });
    }

    @OnClick({R.id.ll_1, R.id.ll_2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_1:
                startActivity(new Intent(this, History1Activity.class).putExtra("code", 1));
                break;
            case R.id.ll_2:
                startActivity(new Intent(this, History1Activity.class).putExtra("code", 2));
                break;
        }
    }
}
