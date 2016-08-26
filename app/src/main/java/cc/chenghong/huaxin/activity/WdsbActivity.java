package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import cc.chenghong.huaxin.entity.Wdsb;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;

public class WdsbActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;

    List<Wdsb> list = new ArrayList<Wdsb>();
    CommonAdapter<Wdsb> adapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_wdsb);
        initXutils();
        setTitleName("我的设备");
        getData(true);
    }

    public void getData(final boolean b) {
        AsyncHttpParams asyncHttpParams = AsyncHttpParams.New();
        asyncHttpParams.put("user_id", App.getUser().getId());
        if (b) load();
        AsyncHttpRequest.post(Api.equipment_page, asyncHttpParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    Wdsb data = fromJson(bytes, Wdsb.class);
                    if (data != null) {
                        if (data.data.size() == 0) {
                            toask("暂无数据");
                            return;
                        }
                        list.clear();
                        list.addAll(data.data);
                        if (adapter == null) {
                            initAdapter();
                        }
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

    private void initAdapter() {
        adapter = new CommonAdapter<Wdsb>(this, list, R.layout.lv_item_wdsb) {
            @Override
            public void convert(ViewHolder helper, Wdsb item, int position) {
                helper.setText(R.id.tv1, item.getName() + "");
                TextView tv2 = helper.getView(R.id.tv2);
                if (!stringIsNull(item.getIm_url())) {
                    getUrlBitmap(item.getIm_url(), (ImageView) helper.getView(R.id.iv));
                } else {
                    helper.setImageResource(R.id.iv, R.drawable.c5);
                }
                if (item.getIs_select()==null||item.getIs_select().equals("0")){
                    tv2.setVisibility(View.VISIBLE);
                }else {
                    tv2.setVisibility(View.GONE);
                }
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                show(position);
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = newIntent(BdsbActivity.class);
                intent.putExtra("data", list.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        getData(false);
        super.onRestart();
    }
    TextView tvContent;
    private void show(final int position) {
        if (list.get(position).getIs_select()==null||list.get(position).getIs_select().equals("0")){
            toask("请先绑定设备号");
            return;
        }
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_ask);
            TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
            tvContent = (TextView) dialog.findViewById(R.id.tv_content);
            tvTitle.setText("确认删除？");
            ViewGroup.LayoutParams lp = tvTitle.getLayoutParams();
            lp.width = getWidth();
            tvTitle.setLayoutParams(lp);
            TextView tvYes = (TextView) dialog.findViewById(R.id.tv_yes);
            TextView tvNo = (TextView) dialog.findViewById(R.id.tv_no);
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    AsyncHttpParams asyncHttpParams = AsyncHttpParams.New();
                    asyncHttpParams.put("user_id", App.getUser().getId());
                    asyncHttpParams.put("id", list.get(position).getId());
                    progress("删除中...");
                    AsyncHttpRequest.post(Api.equipment_delete, asyncHttpParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("删除成功");
                                list.get(position).setIs_select("0");
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            } else {
                                toask("删除失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                }
            });
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        tvContent.setText(list.get(position).getName());
        dialog.show();
    }
}
