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
import cc.chenghong.huaxin.entity.Llbl;
import cc.chenghong.huaxin.entity.Page;
import cc.chenghong.huaxin.view.RefreshViewPD;
import ligth_blue.view.NoScrollGridAdapter;
import ligth_blue.view.NoScrollGridView;

/**
 * 浏览病历
 */
public class LlblActivity extends BaseActivity {
    @ViewInject(R.id.ll_add)
    LinearLayout ll_add;
    @ViewInject(R.id.ll_delete)
    LinearLayout ll_delete;
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.rv)
    RefreshViewPD rv;

    List<Llbl> list = new ArrayList<Llbl>();
    List<Llbl> list_delete = new ArrayList<Llbl>();
    CommonAdapter<Llbl> adapter;
    Dialog dialog;
    Page<Llbl> page = new Page<Llbl>(0,10);
    RequestParams requestParams = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_llbl);
        setTitleName("浏览病例");
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
        rv.openPullDown();
        rv.openPullUp();
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
        adapter = new CommonAdapter<Llbl>(this, list, R.layout.lv_item_llbl) {
            @Override
            public void convert(ViewHolder helper, Llbl item, final int position) {
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
                helper.setText(R.id.tv1, item.getTitle());
                helper.setText(R.id.tv2, item.getCreated());
                ImageView iv = helper.getView(R.id.iv);
//                if (item.getImg_url() != null) {
//                    getUrlBitmap(item.getImg_url(), iv);
//                    iv.setVisibility(View.VISIBLE);
//                } else {
//                    iv.setVisibility(View.GONE);
//                }
                NoScrollGridView gv = helper.getView(R.id.gv);
                final ArrayList<String> list_urls = new ArrayList<String>();
                if (item.getImg_url() != null && !item.getImg_url().equals("")) {
                    String s[] = item.getImg_url().split(",");
                    for (int i = 0; i < s.length; i++) {
                        list_urls.add(s[i]);
                    }
                }
                gv.setAdapter(new NoScrollGridAdapter(LlblActivity.this,list_urls));
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        photoBrower(i, list_urls);
                    }
                });
                //item里的gridview覆盖了listview的onitemclick方法,所以需要重新设置点击事件
                helper.getView(R.id.ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!iv_rigth.isSelected()) {
                            return;
                        }
                        if (list.get(position).isSelect()) {
                            list.get(position).setIsSelect(false);
                        } else {
                            list.get(position).setIsSelect(true);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                toask(1234);
                if (!iv_rigth.isSelected()) {
//                    toask("未选择删除按钮");
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
                tv_rigth.setVisibility(View.VISIBLE);
                iv_rigth.setVisibility(View.GONE);
                iv_rigth.setSelected(true);
                ll_add.setVisibility(View.GONE);
                ll_delete.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_rigth:
                if (tv_rigth.isSelected()) {
                    for (Llbl Llbl : list) {
                        Llbl.setIsSelect(false);
                    }
                    tv_rigth.setSelected(false);
                    tv_rigth.setText("全选");
                } else {
                    for (Llbl Llbl : list) {
                        Llbl.setIsSelect(true);
                    }
                    tv_rigth.setSelected(true);
                    tv_rigth.setText("取消全选");
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_delete:
                String batch_id = "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {
                        if (batch_id.equals("")) {
                            batch_id += "" + list.get(i).getId();
                        } else {
                            batch_id += "," + list.get(i).getId();
                        }
                        list_delete.add(list.get(i));
                    }
                }
                if(batch_id.equals("")){
                    toask("请选择需要删除的数据");
                    return;
                }
                show(batch_id);
                break;
            case R.id.ll_add:
                Intent intent = new Intent(LlblActivity.this, XjblActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    private void show(final String batch_id) {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_suggest5);
            dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("batch_id", batch_id);
                    progress("删除中...");
                    client().post(Api.bl_batch_delete, requestParams, new AsyncHttpResponseHandler() {
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
                                ll_add.setVisibility(View.VISIBLE);
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

    public void getData(final boolean b) {
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
        if (b)load();
        client().post(Api.bl_page, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    if (b)load_succeed();
                    Llbl k = fromJson(bytes, Llbl.class);
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
//                    if (b)load_succeed();
                    if (adapter == null) initAdapter();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {

        if (RESULT_OK == resultCode && requestCode == 1) {
            Llbl k = (Llbl) mIntent.getSerializableExtra("data");
            if (k != null) {
                list.add(k);
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, mIntent);
    }

    @Override
    protected void onRestart() {
        getData(false);
        super.onRestart();
    }
}
