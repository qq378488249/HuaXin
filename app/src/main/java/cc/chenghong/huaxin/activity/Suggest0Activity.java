package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import cc.chenghong.huaxin.entity.Page;
import cc.chenghong.huaxin.entity.Suggest0;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.PullToRefreshLayout;

/**
 * 健康预警
 */
public class Suggest0Activity extends BaseActivity {
    @ViewInject(R.id.ll_delete)
    LinearLayout ll_delete;
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.pl)
    PullToRefreshLayout pl;

    TextView tv;
    List<Suggest0> list = new ArrayList<Suggest0>();
    List<Suggest0> list_delete = new ArrayList<Suggest0>();
    CommonAdapter<Suggest0> adapter;

    Dialog dialog;

    Page<Suggest0> page = new Page<Suggest0>(0, 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_suggest0);
        setTitleName("健康预警");
        initXutils();
        iv_rigth.setImageResource(R.drawable.a_0012_trash_can);
        iv_rigth.setVisibility(View.VISIBLE);
        tv_rigth.setBackgroundResource(R.color.blue_title);
        tv_rigth.setTextColor(getResources().getColor(R.color.white));
        tv_rigth.setText("全选");
        tv_rigth.setTextSize(20);
        ll_delete.setVisibility(View.GONE);
        getData(true, 0);
//        Suggest0 Suggest0;
//        for (int i = 0; i < 5; i++) {
//            Suggest0 = new Suggest0();
//            list.add(Suggest0);
//        }
//        initAdapter();
//        tv_rigth.setVisibility(View.VISIBLE);
    }

    /**
     * @param b
     * @param index 1下拉刷新，2上拉加载更多
     */
    private void getData(boolean b, final int index) {
        AsyncHttpParams requestParams = new AsyncHttpParams();
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
//        requestParams.put("is_notice", 1);
        if (b) progress("加载中...");
        AsyncHttpRequest.post(Api.jkyj_page, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                Suggest0 suggest0 = fromJson(bytes, Suggest0.class);
                if (suggest0.isSuccess()) {
                    if (index == 1) {
                        pl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if (index == 2) {
                        pl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
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
                    if (index == 1) {
                        pl.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                    if (index == 2) {
                        pl.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (index == 1) {
                    pl.refreshFinish(PullToRefreshLayout.FAIL);
                }
                if (index == 2) {
                    pl.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                no_network(throwable);
            }
        });
    }

    private void initAdapter() {
        adapter = new CommonAdapter<Suggest0>(this, list, R.layout.lv_item_suggest0) {
            @Override
            public void convert(ViewHolder helper, Suggest0 item, int position) {
                ImageView iv_delete = helper.getView(R.id.iv_delete);
                if (iv_rigth.isSelected()) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.GONE);
                }
                if (item.isSelect()) {
                    iv_delete.setSelected(true);
                } else {
                    iv_delete.setSelected(false);
                }
                helper.setText(R.id.tv2, item.getCreated());
                TextView tv3 = helper.getView(R.id.tv3);
                switch (item.getValue()) {
                    case 1:
                        helper.setImageResource(R.id.iv1, R.drawable.a_0011_);
                        tv3.setText("很高");
                        tv3.setTextColor(getResources().getColor(R.color.red_bg));
                        break;
                    case 2:
                        helper.setImageResource(R.id.iv1, R.drawable.a_0010_);
                        tv3.setText("偏高");
                        tv3.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case 3:
                        helper.setImageResource(R.id.iv1, R.drawable.a_0009_);
                        tv3.setText("正常");
                        tv3.setTextColor(getResources().getColor(R.color.green));
                        break;
                    case 4:
                        helper.setImageResource(R.id.iv1, R.drawable.a_0010_);
                        tv3.setText("偏低");
                        tv3.setTextColor(getResources().getColor(R.color.yellow));
                        break;
                    case 5:
                        helper.setImageResource(R.id.iv1, R.drawable.a_0011_);
                        tv3.setText("很低");
                        tv3.setTextColor(getResources().getColor(R.color.red_bg));
                        break;
                }
                switch (item.getType()) {
                    case 1:
                        helper.setText(R.id.tv1, "血压");
                        helper.setImageResource(R.id.iv2, R.drawable.a_0029_);
                        break;
                    case 2:
                        helper.setText(R.id.tv1, "血糖");
                        helper.setImageResource(R.id.iv2, R.drawable.a_0028_);
                        break;
                    case 3:
                        helper.setText(R.id.tv1, "体脂");
                        helper.setImageResource(R.id.iv2, R.drawable.a_0027_);
                        break;
                }
                helper.setText(R.id.tv4, item.getContent());
            }
        };
        lv.setAdapter(adapter);
        pl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page.firstPage();
                getData(false, 1);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page.nextPage();
                getData(false, 2);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!iv_rigth.isSelected()) {
                    return;
                }
                if (list.get(i).isSelect()) {
                    list.get(i).setIsSelect(false);
                } else {
                    list.get(i).setIsSelect(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.iv_rigth, R.id.tv_rigth, R.id.ll_delete, R.id.ll_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_rigth:
                if(list.size() == 0){
                    toask("没有可以删除的数据");
                    return;
                }
                tv_rigth.setVisibility(View.VISIBLE);
                iv_rigth.setVisibility(View.GONE);
                iv_rigth.setSelected(true);
                ll_delete.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_rigth:
                if (tv_rigth.isSelected()) {
                    tv_rigth.setSelected(false);
                    for (Suggest0 Suggest0 : list) {
                        Suggest0.setIsSelect(false);
                    }
                    iv_rigth.setSelected(false);
                    iv_rigth.setVisibility(View.VISIBLE);
                    tv_rigth.setVisibility(View.GONE);
                    tv_rigth.setSelected(false);
                    tv_rigth.setText("全选");
//                    adapter.notifyDataSetChanged();
                } else {
                    tv_rigth.setSelected(true);
                    for (Suggest0 Suggest0 : list) {
                        Suggest0.setIsSelect(true);
                    }
                    tv_rigth.setText("取消");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_delete:
                list_delete.clear();
                String batch_id = "";
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).isSelect()){
                        if(batch_id.equals("")){
                            batch_id += "" + list.get(i).getId();
                        }else{
                            batch_id += "," + list.get(i).getId();
                        }
                        list_delete.add(list.get(i));
                    }
                }
                if (batch_id.equals("")){
                    toask("请选择需要删除的数据");
                    return;
                }
                show(batch_id);
                break;
            case R.id.ll_add:
                openActivity(AddDrugActivity.class);
                break;
        }
    }

    private void show(String str) {
        final String batch_id = str;
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_suggest5);
            tv = (TextView) dialog.findViewById(R.id.tv_title);
            tv.setText("确定删除?");
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpParams param = new AsyncHttpParams();
                param.put("batch_id", batch_id);
                progress("删除中...");
                AsyncHttpRequest.post(Api.jkyj_batch_delete, param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        hideProgress();
                        if (i == 200) {
                            toask("删除成功");
                            list.removeAll(list_delete);
                            tv_rigth.setVisibility(View.GONE);
                            iv_rigth.setVisibility(View.VISIBLE);
                            iv_rigth.setSelected(false);
                            ll_delete.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            toask("删除失败");
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        no_network(throwable);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
