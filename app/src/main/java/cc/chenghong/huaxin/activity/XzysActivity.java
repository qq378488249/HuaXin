package cc.chenghong.huaxin.activity;

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
import cc.chenghong.huaxin.entity.Xzys;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.RefreshViewPD;

public class XzysActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.rv)
    RefreshViewPD rv;

    List<Xzys> list = new ArrayList<Xzys>();
    CommonAdapter<Xzys> adapter;
    Page<Xzys> page = new Page<Xzys>(0, 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_xzys);
        setTitleName("选择医生");
        initXutils();
        getData();
    }

    private void getData() {
        AsyncHttpParams requestParams = AsyncHttpParams.New();
        requestParams.put("organization_id", App.getUser().getOrganization_id());
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
        progress("加载中...");
        AsyncHttpRequest.post(Api.employee_page, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                Xzys suggest0 = fromJson(bytes, Xzys.class);
                if (suggest0.isSuccess()) {
                    if (adapter == null) {
                        initAdapter();
                    }
                    if (suggest0.data.size() == 0) {
                        toask("暂无数据");
                        return;
                    }
                    if (page.page_index == 0) {
                        list.clear();
                        list.addAll(suggest0.data);
                    } else {
                        list.addAll(suggest0.data);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });

    }

    private void initAdapter() {
        rv.openPullDown();
        rv.openPullUp();
        rv.setListViewScrollListener(lv);
        rv.addOnSnapListener(new RefreshViewPD.OnSnapListener() {
            @Override
            public void onSnapToTop(int distance) {
                progress("加载中...");
                page.firstPage();
                getData();
            }

            @Override
            public void onSnapToBottom(int distance) {
                progress("加载中...");
                page.nextPage();
                getData();
            }
        });
        adapter = new CommonAdapter<Xzys>(this, list, R.layout.lv_item_xzys1) {
            @Override
            public void convert(ViewHolder helper, Xzys item, int position) {
                helper.setText(R.id.tv_name, item.getNickname());
                helper.setText(R.id.tv_ks, item.getSubject());
                helper.setText(R.id.tv_sc, item.getSpecialty());
                ImageView iv = helper.getView(R.id.iv);
                getUrlBitmap(item.getHead_url(), iv);
                ImageView iv_zj = helper.getView(R.id.iv_zj);
                if (item.getIs_expert().equals("0")) {
                    iv_zj.setVisibility(View.GONE);
                } else {
                    iv_zj.setVisibility(View.VISIBLE);
                }
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progress("选择医生中...");
                AsyncHttpParams requestParams = AsyncHttpParams.New();
                requestParams.put("title",getIntent().getStringExtra("title"));
                requestParams.put("content", getIntent().getStringExtra("content"));
                requestParams.put("user_id", App.getUser().getId());
                requestParams.put("employee_id", list.get(i).getId());
                if(!getIntent().getStringExtra("img_url").equals("")){
                    requestParams.put("img_url", getIntent().getStringExtra("img_url"));
                }
                AsyncHttpRequest.post(Api.consultation_add, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        hideProgress();
                        if (i == 200) {
                            toask("选择医生成功");
                            setResult(RESULT_OK, newIntent(ConsultActivity.class));
                            finish();
                        } else {
                            toask("选择医生失败");
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        no_network(throwable);
                    }
                });
            }
        });
    }

}
