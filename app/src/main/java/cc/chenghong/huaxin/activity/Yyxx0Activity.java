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
import cc.chenghong.huaxin.entity.Yyxx0;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.RefreshViewPD;

public class Yyxx0Activity extends BaseActivity {
    @ViewInject(R.id.ll_add)
    LinearLayout ll_add;
    @ViewInject(R.id.ll_delete)
    LinearLayout ll_delete;
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.rv)
    RefreshViewPD rv;

    Page<Yyxx0> page = new Page<Yyxx0>(0, 10);

    List<Yyxx0> list = new ArrayList<Yyxx0>();
    List<Yyxx0> list_delete = new ArrayList<Yyxx0>();
    CommonAdapter<Yyxx0> adapter;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_yyxx0);
        setTitleName("用药信息");
        initXutils();
        iv_rigth.setImageResource(R.drawable.a_0012_trash_can);
        iv_rigth.setVisibility(View.VISIBLE);
        tv_rigth.setBackgroundResource(R.color.blue_title);
        tv_rigth.setTextColor(getResources().getColor(R.color.white));
        tv_rigth.setText("全选");
        tv_rigth.setTextSize(20);
        ll_delete.setVisibility(View.GONE);
        ll_add.setVisibility(View.VISIBLE);

        getData(true);
    }

    private void getData(final boolean b) {
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
        if (b)progress("加载中...");
        AsyncHttpRequest.post(Api.yyxx_page, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                Yyxx0 k = fromJson(bytes, Yyxx0.class);
                if (k.isSuccess()) {
                    if (adapter == null) initAdapter();
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
                    adapter.notifyDataSetChanged();
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

    private void initAdapter() {
        rv.openPullUp();
        rv.openPullDown();
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
                getData(false);
            }
        });
        adapter = new CommonAdapter<Yyxx0>(this, list, R.layout.lv_item_yyxx0) {
            @Override
            public void convert(ViewHolder helper, final Yyxx0 item, final int position) {
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
                helper.setText(R.id.tv_name,item.getTitle());
                helper.setText(R.id.tv_count,item.getEat_num());
                TextView tv1 = helper.getView(R.id.tv_time1);
                TextView tv2 = helper.getView(R.id.tv_time2);
                TextView tv3 = helper.getView(R.id.tv_time3);
                TextView tv_fytx = helper.getView(R.id.tv_fytx);

                if(item.getIs_notice().equals("0")){//是否服药提醒
                    tv_fytx.setVisibility(View.GONE);
                }else{
                    tv_fytx.setVisibility(View.VISIBLE);
                }
                if(item.getZaoshang_time() == null){
                    tv1.setVisibility(View.GONE);
                }else{
                    tv1.setVisibility(View.VISIBLE);
                }
                if(item.getZhongwu_time() == null){
                    tv2.setVisibility(View.GONE);
                }else{
                    tv2.setVisibility(View.VISIBLE);
                }
                if(item.getWanshang_time() == null){
                    tv3.setVisibility(View.GONE);
                }else{
                    tv3.setVisibility(View.VISIBLE);
                }
                ImageView iv =helper.getView(R.id.iv);
                if (item.getImg_url()!=null && !item.getImg_url().equals("")){
                    getUrlBitmap(item.getImg_url(),iv);
                    iv.setVisibility(View.VISIBLE);
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> list = new ArrayList<String>();
                            String str[] = item.getImg_url().split(",");
                            for (int i = 0; i < str.length; i++) {
                                list.add(str[i]);
                            }
                            photoBrower(0,list);
                        }
                    });
                }else{
                    iv.setVisibility(View.GONE);
                }
            }
        };
        lv.setAdapter(adapter);
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
                if (list.size() == 0){
                    toask("没有数据可以删除");
                    return;
                }
                tv_rigth.setVisibility(View.VISIBLE);
                iv_rigth.setVisibility(View.GONE);
                iv_rigth.setSelected(true);
//                for (Yyxx0 yyxx0 : list) {
//                    yyxx0.setIsDelete(true);
//                }
                ll_add.setVisibility(View.GONE);
                ll_delete.setVisibility(View.VISIBLE);
                if (adapter != null) adapter.notifyDataSetChanged();
                break;
            case R.id.tv_rigth:
                if (tv_rigth.isSelected()) {
                    for (Yyxx0 y : list) {
                        y.setIsSelect(false);
                    }
                    tv_rigth.setSelected(false);
                    tv_rigth.setText("全选");
                } else {
                    for (Yyxx0 y : list) {
                        y.setIsSelect(true);
                    }
                    tv_rigth.setSelected(true);
                    tv_rigth.setText("取消全选");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_delete:
                list_delete.clear();
                String s = "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {
                        if (i == 0) {
                            s += "" + list.get(i).getId();
                        } else {
                            s += "," + list.get(i).getId();
                        }
                        list_delete.add(list.get(i));
                    }
                }
                if (s.equals("")) {
                    toask("请选择需要删除的数据");
                    return;
                }
                show(s);
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
            dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progress("删除中...");
                    requestParams.put("batch_id", batch_id);
                    AsyncHttpRequest.post(Api.yyxx_batch_delete, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("删除成功");

                                list.removeAll(list_delete);
                                iv_rigth.setSelected(false);
                                iv_rigth.setVisibility(View.VISIBLE);
                                tv_rigth.setVisibility(View.GONE);
                                ll_add.setVisibility(View.VISIBLE);
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
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    @Override
    protected void onRestart() {
        getData(false);
        super.onRestart();
    }
}
