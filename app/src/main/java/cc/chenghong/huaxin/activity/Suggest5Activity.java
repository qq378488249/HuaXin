package cc.chenghong.huaxin.activity;

import android.app.Dialog;
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
import cc.chenghong.huaxin.entity.Page;
import cc.chenghong.huaxin.entity.Suggest5;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.RefreshViewPD;
import ligth_blue.view.NoScrollGridAdapter;
import ligth_blue.view.NoScrollGridView;

/**
 * 服药提醒
 */
public class Suggest5Activity extends BaseActivity {
    @ViewInject(R.id.rv)
    RefreshViewPD rv;
    @ViewInject(R.id.lv)
    ListView lv;

    TextView tv;

    List<Suggest5> list = new ArrayList<Suggest5>();
    List<Suggest5> list_delete = new ArrayList<Suggest5>();
    CommonAdapter<Suggest5> adapter;
    Page<Suggest5> page = new Page<Suggest5>(0, 10);

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_suggest5);
        setTitleName("服药提醒");
        initXutils();
        getData(true);
    }

    private void getData(final boolean b) {
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
        requestParams.put("is_notice", 1);
        progress("加载中...");
        AsyncHttpRequest.post(Api.yyxx_page, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                Suggest5 k = fromJson(bytes, Suggest5.class);
                if (k.isSuccess()) {
//                    if (b) load_succeed();
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
        adapter = new CommonAdapter<Suggest5>(this, list, R.layout.lv_item_suggest5) {
            @Override
            public void convert(ViewHolder helper, Suggest5 item, final int position) {
                ImageView iv1 = helper.getView(R.id.iv1);
                ImageView iv2 = helper.getView(R.id.iv2);
                TextView tv1 = helper.getView(R.id.tv1);
                TextView tv2 = helper.getView(R.id.tv2);
                TextView tv3 = helper.getView(R.id.tv3);
                TextView tv4 = helper.getView(R.id.tv4);
                helper.setText(R.id.tv1, item.getTitle());
                helper.setText(R.id.tv2, item.getZaoshang_num() + "片 " + item.getZaoshang_time());
                helper.setText(R.id.tv3, item.getZhongwu_num() + "片 " + item.getZhongwu_time());
                helper.setText(R.id.tv4, item.getWanshang_num() + "片 " + item.getWanshang_time());
                if (item.getIs_notice().equals("1")) {//服药提醒
                    helper.getView(R.id.tv2).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv3).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv4).setVisibility(View.VISIBLE);
                    if(item.getZaoshang_time() == null){
                        helper.getView(R.id.tv2).setVisibility(View.GONE);
                    }
                    if(item.getZhongwu_time() == null){
                        helper.getView(R.id.tv3).setVisibility(View.GONE);
                    }
                    if(item.getWanshang_time() == null){
                        helper.getView(R.id.tv4).setVisibility(View.GONE);
                    }
                } else {
                    helper.getView(R.id.tv2).setVisibility(View.GONE);
                    helper.getView(R.id.tv3).setVisibility(View.GONE);
                    helper.getView(R.id.tv4).setVisibility(View.GONE);
                }
//                if (stringIsNull(item.getImg_url())) {
//                    iv2.setVisibility(View.GONE);
//                } else {
//                    iv2.setVisibility(View.VISIBLE);
//                    getUrlBitmap(item.getImg_url(), iv2);
//                }
                helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show(position);
                    }
                });
                NoScrollGridView gv = helper.getView(R.id.gv);
                final ArrayList<String> list_urls = new ArrayList<String>();
                if (item.getImg_url() != null && !item.getImg_url().equals("")) {
                    String s[] = item.getImg_url().split(",");
                    for (int i = 0; i < s.length; i++) {
                        list_urls.add(s[i]);
                    }
                }
                gv.setAdapter(new NoScrollGridAdapter(Suggest5Activity.this,list_urls));
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        photoBrower(i, list_urls);
                    }
                });
            }
        };
        lv.setAdapter(adapter);
    }

    private void show(final int index) {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_suggest5);
            tv = (TextView) dialog.findViewById(R.id.tv_title);
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
                AsyncHttpParams requestParams = new AsyncHttpParams();
                requestParams.put("id", list.get(index).getId());
//                list_delete.clear();
//                list_delete.add(list.get(i));
                AsyncHttpRequest.post(Api.yyxx_delete, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        hideProgress();
                        if (i == 200) {
                            toask("删除成功");
                            list.remove(index);
                            adapter.notifyDataSetChanged();
//                            list.removeAll(list_delete);
//                            adapter.notifyDataSetChanged();
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
        tv.setText("确定要删除" + list.get(index).getTitle() + "的服药提醒?");
        dialog.show();
    }

}
