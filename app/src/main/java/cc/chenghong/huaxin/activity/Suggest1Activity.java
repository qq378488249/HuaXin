package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Page;
import cc.chenghong.huaxin.entity.Suggest1;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.utils.StringUtils;
import cc.chenghong.huaxin.view.RefreshViewPD;

/**
 * 1营养方案、3运动建议、5健康贴士
 */
public class Suggest1Activity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.rv)
    RefreshViewPD rv;

    CommonAdapter<Suggest1> adatper;
    List<Suggest1> list = new ArrayList<Suggest1>();
    String url;


    Page<Suggest1> page = new Page<Suggest1>(0, 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_suggest1);
        initXutils();
//        initAdapter();
        switch (code) {
            case 1:
                url = Api.yyfa_page;
                setTitleName("营养方案");
                break;
            case 3:
                url = Api.ydjy_page;
                setTitleName("运动建议");
                break;
            case 4:
                url = Api.jkts_page;
                setTitleName("健康贴士");
                break;
        }
        getData(true);
    }

    private void initAdapter() {
//        rv.openPullDown();
//        rv.openPullUp();
        rv.setListViewScrollListener(lv);
        rv.addOnSnapListener(new RefreshViewPD.OnSnapListener() {
            @Override
            public void onSnapToTop(int distance) {
                page.firstPage();
                getData(true);
            }

            @Override
            public void onSnapToBottom(int distance) {
                page.nextPage();
                getData(true);
            }
        });
        adatper = new CommonAdapter<Suggest1>(this, list, R.layout.lv_item_suggest1) {
            @Override
            public void convert(ViewHolder helper, Suggest1 item, int position) {
//                ImageView iv = helper.getView(R.id.iv);
//                setViewHeigth(iv,getWidth() - UITools.dip2px(20));
                getUrlBitmap(item.getImg_url(), (ImageView) helper.getView(R.id.iv));
                helper.setText(R.id.tv1, formatDate(item.getCreated()));
                helper.setText(R.id.tv2, StringUtils.html2Android(item.getTitle()));
                helper.setText(R.id.tv3, StringUtils.html2Android(item.getSmall_title()));
            }
        };
        lv.setAdapter(adatper);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Jkts1Activity.class);
                intent.putExtra("id", list.get(i).getId());
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });
    }

    public void getData(final boolean b) {
        AsyncHttpParams requestParams = new AsyncHttpParams();
        requestParams.put("user_id", App.getUser().getId());
//        requestParams.put("page_size", page.page_size);
//        requestParams.put("page_index", page.page_index);
        progress("加载中...");
        AsyncHttpRequest.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                Suggest1 k = fromJson(bytes, Suggest1.class);
                if (k.isSuccess()) {
                    if (k.data.size() == 0) {
                        toask("暂无数据");
                        return;
                    }
                    if (page.page_index == 0) {
                        list.clear();
                        list.addAll(k.data);
                    } else {
                        list.addAll(k.data);
                    }
//                    toask("加载成功");
                    if (adatper == null) initAdapter();
                    adatper.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }
}
