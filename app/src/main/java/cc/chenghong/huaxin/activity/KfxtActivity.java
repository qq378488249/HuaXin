package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Kfxt;
import cc.chenghong.huaxin.entity.Page;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.RefreshViewPD;

/**
 * 空腹血糖
 */
public class KfxtActivity extends BaseActivity {
    @ViewInject(R.id.ll_add)
    LinearLayout ll_add;
    @ViewInject(R.id.ll_delete)
    LinearLayout ll_delete;
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.rv)
    RefreshViewPD rv;

    List<Kfxt> list = new ArrayList<Kfxt>();
    List<Kfxt> list_delete = new ArrayList<Kfxt>();
    CommonAdapter<Kfxt> adapter;
    Page<Kfxt> page = new Page<Kfxt>(0, 10);

    Dialog dialog;
    //获取数据地址
    String get_url = "";
    //删除数据地址
    String delete_url = "";
    //添加数据地址
    String add_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_kfxt);
        setTitleName("空腹血糖");
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

    private void initAdapter() {
        rv.openPullUp();
        rv.openPullDown();
        rv.setListViewScrollListener(lv);
        rv.addOnSnapListener(new RefreshViewPD.OnSnapListener() {
            @Override
            public void onSnapToTop(int distance) {
                progress("加载中...");
                page.firstPage();
                getData(true);
            }

            @Override
            public void onSnapToBottom(int distance) {
                progress("加载中...");
                page.nextPage();
                getData(false);
            }
        });
        adapter = new CommonAdapter<Kfxt>(this, list, R.layout.lv_item_kfxt) {
            @Override
            public void convert(ViewHolder helper, Kfxt item, int position) {
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
                helper.setText(R.id.tv1,item.getValue()+"mmol/L");
                if (code == 10){
                    helper.setText(R.id.tv1,item.getValue()+"%");
                }
//                String s = item.getCreated().substring(0, item.getCreated().length() - 3);
                helper.setText(R.id.tv2,formatDate(item.getCreated()));
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
                if(list.size() == 0){
                    toask("没有可以删除的数据");
                    return;
                }
                tv_rigth.setVisibility(View.VISIBLE);
                iv_rigth.setVisibility(View.GONE);
                iv_rigth.setSelected(true);
                ll_add.setVisibility(View.GONE);
                ll_delete.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_rigth:
                if (tv_rigth.isSelected()) {
                    for (Kfxt Kfxt : list) {
                        Kfxt.setIsSelect(false);
                    }
                    iv_rigth.setSelected(false);
                    iv_rigth.setVisibility(View.VISIBLE);
                    tv_rigth.setVisibility(View.GONE);
                    tv_rigth.setSelected(false);
                    tv_rigth.setText("全选");
                    ll_add.setVisibility(View.VISIBLE);
                    ll_delete.setVisibility(View.GONE);
//                    tv_rigth.setSelected(false);
//                    tv_rigth.setText("全选");
                } else {
                    for (Kfxt Kfxt : list) {
                        Kfxt.setIsSelect(true);
                    }
                    tv_rigth.setSelected(true);
                    tv_rigth.setText("取消");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_delete:
                list_delete.clear();
                String s = "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {
                        if (s.equals("")) {
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
                System.out.println(s);
                show(s);
                break;
            case R.id.ll_add:
                Intent intent = new Intent(getApplicationContext(),XgxmActivity.class);
                intent.putExtra("code",code);
                intent.putExtra("add_url",add_url);
                startActivityForResult(intent,1);
//                openActivityForResult(XgxmActivity.class, 1, code);
                break;
        }
    }

    private void show(String s) {
        final String str  = s;
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_suggest5);
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
                progress("删除中...");
                RequestParams requestParams = new RequestParams();
                requestParams.put("user_id", App.getUser().getId());
                requestParams.put("batch_id", str);
                log_i(delete_url);
                log_i(App.getUser().getId());
                log_i(str);
                client().post(delete_url, requestParams, new AsyncHttpResponseHandler() {
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
                            tv_rigth.setSelected(false);
                            tv_rigth.setText("全选");
                            adapter.notifyDataSetChanged();
                        } else {
                            toask("删除失败");
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        no_network();
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void getData(final boolean b) {
        switch (code) {
            case 6:
                setTitleName("空腹血糖");
                get_url = Api.kfxt_page;
                delete_url = Api.kfxt_batch_delete;
                add_url = Api.kfxt_add;
                break;
            case 7:
                setTitleName("总胆固醇");
                get_url = Api.zdgc_page;
                delete_url = Api.zdgc_batch_delete;
                add_url = Api.zdgc_add;
                break;
            case 8:
                setTitleName("甘油三酯");
                get_url = Api.gysz_page;
                delete_url = Api.gysz_batch_delete;
                add_url = Api.gysz_add;
                break;
            case 9:
                setTitleName("高密度脂蛋白");
                get_url = Api.gmdzdb_page;
                delete_url = Api.gmdzdb_batch_delete;
                add_url = Api.gmdzdb_add;
                break;
            case 10:
                setTitleName("糖化血红蛋白");
                get_url = Api.thxhdb_page;
                delete_url = Api.thxhdb_batch_delete;
                add_url = Api.thxhdb_add;
                break;
        }
        if (b)progress("加载中...");
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
        AsyncHttpRequest.post(get_url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    if (b) toask("加载成功");
                    Kfxt k = fromJson(bytes, Kfxt.class);
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
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (RESULT_OK == resultCode && requestCode == 1) {
            getData(false);
        }
        super.onActivityResult(requestCode, resultCode, mIntent);
    }
}
