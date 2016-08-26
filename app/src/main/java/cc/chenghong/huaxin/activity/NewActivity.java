package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.Intent;
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
import cc.chenghong.huaxin.entity.Suggest0;
import cc.chenghong.huaxin.entity.Yyxx0;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.RefreshViewPD;
import ligth_blue.view.NoScrollGridAdapter;
import ligth_blue.view.NoScrollGridView;

/**
 * 浏览病例、用药信息
 */
public class NewActivity extends BaseActivity {
    @ViewInject(R.id.ll_add)
    LinearLayout ll_add;
    @ViewInject(R.id.ll_delete)
    LinearLayout ll_delete;
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.rv)
    RefreshViewPD rv;
    @ViewInject(R.id.tv_add)
    TextView tv_add;
    @ViewInject(R.id.tv_delete)
    TextView tv_delete;

    List list_delete = new ArrayList();
    Dialog dialog;
    Page page;
    String getDataUrl;
    String deleteUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_llbl);
        initXutils();
        switch (code) {
            case 1:
                setTitleName("浏览病例");
                page = new Page<Llbl>(0, 10);
                getDataUrl = Api.bl_page;
                deleteUrl = Api.bl_batch_delete;
                break;
            case 2://用药信息
                setTitleName("用药信息");
                 page = new Page<Yyxx0>(0, 10);
                getDataUrl = Api.yyxx_page;
                deleteUrl = Api.yyxx_batch_delete;
                tv_add.setText("添加用药信息");
                tv_delete.setText("删除用药信息");
                break;
            case 3:
                setTitleName("健康预警");
                page = new Page<Suggest0>(0, 10);
                getDataUrl = Api.jkyj_page;
                deleteUrl = Api.jkyj_batch_delete;
                ll_add.setVisibility(View.GONE);
                tv_delete.setText("删除健康预警");
                ll_delete.setVisibility(View.GONE);
                break;
        }
        iv_rigth.setImageResource(R.drawable.a_0012_trash_can);
        iv_rigth.setVisibility(View.VISIBLE);
        tv_rigth.setBackgroundResource(R.color.blue_title);
        tv_rigth.setTextColor(getResources().getColor(R.color.white));
        tv_rigth.setText("全选");
        tv_rigth.setTextSize(20);
//        ll_delete.setVisibility(View.GONE);
//        ll_add.setVisibility(View.VISIBLE);
        initAdapter();
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
                getData(true);
            }
        });
        if (code == 3) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rv.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
        }
        switch (code) {
            case 1:
                page.adapter = new CommonAdapter<Llbl>(this, page.list, R.layout.lv_item_llbl) {
                    @Override
                    public void convert(ViewHolder helper, final Llbl item, final int position) {
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
                        helper.setText(R.id.tv2, item.getTime());
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
                        gv.setAdapter(new NoScrollGridAdapter(NewActivity.this, list_urls));
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
                                if (item.isSelect()) {
                                    item.setIsSelect(false);
                                } else {
                                    item.setIsSelect(true);
                                }
                                page.adapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
                break;
            case 2:
                page.adapter = new CommonAdapter<Yyxx0>(this, page.list, R.layout.lv_item_yyxx0) {
                    @Override
                    public void convert(ViewHolder helper, final Yyxx0 item, final int position) {
                        final ImageView iv_delete = helper.getView(R.id.iv_delete);
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
                        helper.getView(R.id.ll).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (iv_rigth.isSelected()) {
                                    if (item.isSelect()){
                                        item.setIsSelect(false);
                                    }else{
                                        item.setIsSelect(true);
                                    }
                                    page.adapter.notifyDataSetChanged();
                                } else {
                                    Intent intent = newIntent(AddDrugActivity.class);
                                    intent.putExtra("data", item);
                                    intent.putExtra("code", 2);
                                    startActivity(intent);
                                }
                            }
                        });
                        helper.setText(R.id.tv_name, item.getTitle());
                        helper.setText(R.id.tv_count, item.getEat_num());
                        TextView tv1 = helper.getView(R.id.tv_time1);
                        TextView tv2 = helper.getView(R.id.tv_time2);
                        TextView tv3 = helper.getView(R.id.tv_time3);
                        TextView tv_fytx = helper.getView(R.id.tv_fytx);
                        if (item.getIs_notice().equals("0")) {//是否服药提醒
                            tv_fytx.setVisibility(View.GONE);
                        } else {
                            tv_fytx.setVisibility(View.VISIBLE);
                        }
                        if (item.getZaoshang_time() == null) {
                             tv1.setVisibility(View.GONE);
                        } else {
                            tv1.setVisibility(View.VISIBLE);
                            String[] s= item.getZaoshang_time().split(":");
                            setSjd(Integer.valueOf(s[0]),(TextView)helper.getView(R.id.tv_time1));
                        }
                        if (item.getZhongwu_time() == null) {
                            tv2.setVisibility(View.GONE);
                        } else {
                            String[] s= item.getZhongwu_time().split(":");
                            setSjd(Integer.valueOf(s[0]),(TextView)helper.getView(R.id.tv_time2));
                            tv2.setVisibility(View.VISIBLE);
                        }
                        if (item.getWanshang_time() == null) {
                            tv3.setVisibility(View.GONE);
                        } else {
                            String[] s= item.getWanshang_time().split(":");
                            setSjd(Integer.valueOf(s[0]),(TextView)helper.getView(R.id.tv_time3));
                            tv3.setVisibility(View.VISIBLE);
                        }
//                        ImageView iv = helper.getView(R.id.iv);
//                        if (item.getImg_url() != null && !item.getImg_url().equals("")) {
//                            getUrlBitmap(item.getImg_url(), iv);
//                            iv.setVisibility(View.VISIBLE);
//                            iv.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    ArrayList<String> list = new ArrayList<String>();
//                                    String str[] = item.getImg_url().split(",");
//                                    for (int i = 0; i < str.length; i++) {
//                                        list.add(str[i]);
//                                    }
//                                    photoBrower(0, list);
//                                }
//                            });
//                        } else {
//                            iv.setVisibility(View.GONE);
//                        }
                        NoScrollGridView gv = helper.getView(R.id.gv);
                        final ArrayList<String> list_urls = new ArrayList<String>();
                        if (item.getImg_url() != null && !item.getImg_url().equals("")) {
                            String s[] = item.getImg_url().split(",");
                            for (int i = 0; i < s.length; i++) {
                                list_urls.add(s[i]);
                            }
                        }
                        gv.setAdapter(new NoScrollGridAdapter(getContext(), list_urls));
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                photoBrower(i, list_urls);
                            }
                        });
                    }
                };
                break;
            case 3:
                page.adapter = new CommonAdapter<Suggest0>(this, page.list, R.layout.lv_item_suggest0) {
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
                                tv3.setTextColor(getResources().getColor(R.color.blue_title));
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
                                tv3.setTextColor(getResources().getColor(R.color.blue_title));
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
                lv.setDivider(null);
                lv.setAdapter(page.adapter);
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        if (!iv_rigth.isSelected()) {
//                            return;
//                        }
//                        if (((Suggest0)page.list.get(i)).isSelect()) {
//                            ((Suggest0)page.list.get(i)).setIsSelect(false);
//                        } else {
//                            ((Suggest0)page.list.get(i)).setIsSelect(true);
//                        }
//                        page.adapter.notifyDataSetChanged();
//                    }
//                });
                break;
        }

        lv.setAdapter(page.adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!iv_rigth.isSelected()) {//没有选择删除按钮不做事件处理
                    return;
                }
                switch (code) {
                    case 1:
                        Llbl item = (Llbl) page.list.get(i);
                        if (item.isSelect()) {
                            item.setIsSelect(false);
                        } else {
                            item.setIsSelect(true);
                        }
                        break;
                    case 2:
                        Yyxx0 item1 = (Yyxx0) page.list.get(i);
                        if (item1.isSelect()) {
                            item1.setIsSelect(false);
                        } else {
                            item1.setIsSelect(true);
                        }
                        break;
                    case 3:
                        Suggest0 item2 = (Suggest0) page.list.get(i);
                        if (item2.isSelect()) {
                            item2.setIsSelect(false);
                        } else {
                            item2.setIsSelect(true);
                        }
                        break;
                }
                page.adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.iv_rigth, R.id.tv_rigth, R.id.ll_delete, R.id.ll_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_rigth:
                if (page.list.size() == 0) {
                    toask("没有可以删除的数据");
                    return;
                }
                tv_rigth.setVisibility(View.VISIBLE);
                iv_rigth.setVisibility(View.GONE);
                iv_rigth.setSelected(true);
                ll_add.setVisibility(View.GONE);
                ll_delete.setVisibility(View.VISIBLE);
                page.adapter.notifyDataSetChanged();
                break;
            case R.id.tv_rigth:
                if (tv_rigth.isSelected()) {
                    for (int i = 0; i < page.list.size(); i++) {
                        switch (code) {
                            case 1:
                                Llbl llbl = (Llbl) page.list.get(i);
                                llbl.setIsSelect(false);
                                break;
                            case 2:
                                Yyxx0 y = (Yyxx0) page.list.get(i);
                                y.setIsSelect(false);
                                break;
                            case 3:
                                Suggest0 s = (Suggest0) page.list.get(i);
                                s.setIsSelect(false);
                                break;
                        }
                    }
                    iv_rigth.setSelected(false);
                    iv_rigth.setVisibility(View.VISIBLE);
                    tv_rigth.setVisibility(View.GONE);
                    tv_rigth.setSelected(false);
                    tv_rigth.setText("全选");
                    ll_add.setVisibility(View.VISIBLE);
                    if (code == 3){
                        ll_add.setVisibility(View.GONE);
                    }
                    ll_delete.setVisibility(View.GONE);
//                    tv_rigth.setSelected(false);
//                    tv_rigth.setText("全选");
                } else {
                    for (int i = 0; i < page.list.size(); i++) {
                        switch (code) {
                            case 1:
                                Llbl llbl = (Llbl) page.list.get(i);
                                llbl.setIsSelect(true);
                                break;
                            case 2:
                                Yyxx0 y = (Yyxx0) page.list.get(i);
                                y.setIsSelect(true);
                                break;
                            case 3:
                                Suggest0 s = (Suggest0) page.list.get(i);
                                s.setIsSelect(true);
                                break;
                        }
                    }
                    tv_rigth.setSelected(true);
                    tv_rigth.setText("取消");
                }
                page.adapter.notifyDataSetChanged();
                break;
            case R.id.ll_delete:
                String batch_id = "";
                for (int i = 0; i < page.list.size(); i++) {
                    if (code == 1) {
                        Llbl item = (Llbl) page.list.get(i);
                        if (item.isSelect()) {
                            if (batch_id.equals("")) {
                                batch_id += "" + item.getId();
                            } else {
                                batch_id += "," + item.getId();
                            }
                            list_delete.add(page.list.get(i));
                        }
                    }
                    if (code == 2) {
                        Yyxx0 item = (Yyxx0) page.list.get(i);
                        if (item.isSelect()) {
                            if (batch_id.equals("")) {
                                batch_id += "" + item.getId();
                            } else {
                                batch_id += "," + item.getId();
                            }
                            list_delete.add(page.list.get(i));
                        }
                    }
                    if (code == 3) {
                        Suggest0 item = (Suggest0) page.list.get(i);
                        if (item.isSelect()) {
                            if (batch_id.equals("")) {
                                batch_id += "" + item.getId();
                            } else {
                                batch_id += "," + item.getId();
                            }
                            list_delete.add(page.list.get(i));
                        }
                    }
                }
                if (batch_id.equals("")) {
                    toask("请选择需要删除的数据");
                    return;
                }
                show(batch_id);
                break;
            case R.id.ll_add:
                switch (code) {
                    case 1:
                        openActivity(XjblActivity.class);
                        break;
                    case 2:
                        openActivity(AddDrugActivity.class);
                        break;
                }
                break;
        }
    }

    private void show(final String batch_id) {
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
                RequestParams requestParams = new RequestParams();
                requestParams.put("batch_id", batch_id);
                progress("删除中...");
                client().post(deleteUrl, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        hideProgress();
                        if (i == 200) {
                            toask("删除成功");
                            page.list.removeAll(list_delete);
                            tv_rigth.setVisibility(View.GONE);
                            iv_rigth.setVisibility(View.VISIBLE);
                            iv_rigth.setSelected(false);
                            ll_delete.setVisibility(View.GONE);
                            if (code != 3) {
                                ll_add.setVisibility(View.VISIBLE);
                            } else {
                                ll_add.setVisibility(View.GONE);
                            }
                            page.adapter.notifyDataSetChanged();
                            tv_rigth.setSelected(false);
                            tv_rigth.setText("全选");
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

    public void getData(final boolean b) {
        AsyncHttpParams requestParams = AsyncHttpParams.New();
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
//        if (code == 3){
//            requestParams.put();
//        }
//        switch (code) {
//            case 1:
//                requestParams.put("user_id", App.getUser().getId());
//                requestParams.put("page_index", page.page_index);
//                requestParams.put("page_size", page.page_size);
//                break;
//            case 2:
//                requestParams.put("user_id", App.getUser().getId());
//                requestParams.put("page_index", page.page_index);
//                requestParams.put("page_size", page.page_size);
//                break;
//        }
        if (b) load();
        AsyncHttpRequest.post(getDataUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    switch (code) {
                        case 1:
                            Llbl k = fromJson(bytes, Llbl.class);
                            if (k.data.size() == 0) {
                                toask("暂无数据");
                                return;
                            }
                            if (page.page_index == 0) {
                                page.list.clear();
                                page.list.addAll(k.data);
                            } else {
                                page.list.addAll(k.data);
                            }
                            break;
                        case 2:
                            Yyxx0 k2 = fromJson(bytes, Yyxx0.class);
                            if (k2.data.size() == 0) {
                                toask("暂无数据");
                                return;
                            }
                            if (page.page_index == 0) {
                                page.list.clear();
                                page.list.addAll(k2.data);
                            } else {
                                page.list.addAll(k2.data);
                            }
                            break;
                        case 3:
                            Suggest0 k3 = fromJson(bytes, Suggest0.class);
                            if (k3.data.size() == 0) {
                                toask("暂无数据");
                                return;
                            }
                            if (page.page_index == 0) {
                                page.list.clear();
                                page.list.addAll(k3.data);
                            } else {
                                page.list.addAll(k3.data);
                            }
                            break;
                    }
//                    if (b) load_succeed();
                    if (page.adapter == null) initAdapter();
                    page.adapter.notifyDataSetChanged();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
//
//        if (RESULT_OK == resultCode && requestCode == 1) {
//            T k = (T) mIntent.getSerializableExtra("data");
//            if (k != null) {
//                list.add(k);
//                adapter.notifyDataSetChanged();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, mIntent);
//    }

    @Override
    protected void onRestart() {
        page.firstPage();
        getData(false);
        super.onRestart();
    }

}
