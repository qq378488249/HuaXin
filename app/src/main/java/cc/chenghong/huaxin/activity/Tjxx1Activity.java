package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Allergy;
import cc.chenghong.huaxin.entity.Fys;
import cc.chenghong.huaxin.entity.Tjxx;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.FlowLayout;
import cc.chenghong.huaxin.view.TagAdapter;
import cc.chenghong.huaxin.view.TagFlowLayout;

/**
 * 体检信息
 * 20160320
 * hcl
 */
public class Tjxx1Activity extends BaseActivity {
    @ViewInject(R.id.ll_bar)
    LinearLayout ll_bar;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.tv1)
    TextView tv1;
    @ViewInject(R.id.tv2)
    TextView tv2;
    @ViewInject(R.id.tv3)
    TextView tv3;
    @ViewInject(R.id.tv4)
    TextView tv4;
    @ViewInject(R.id.tv5)
    TextView tv5;
    @ViewInject(R.id.tv_a1)
    TextView tv_a1;
    @ViewInject(R.id.tv_a2)
    TextView tv_a2;
    @ViewInject(R.id.iv_a1)
    ImageView iv_a1;
    @ViewInject(R.id.iv_a2)
    ImageView iv_a2;

    Dialog dialog_fy;
    TagAdapter<Allergy> tagAdapter;
    List<Allergy> list_fy = new ArrayList<Allergy>();
    List<Allergy> list_fy_select = new ArrayList<Allergy>();
    List<Allergy> list_delete = new ArrayList<Allergy>();
    Tjxx t;
    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tjxx);
        initXutils();
        statusBar(ll_bar);
        tv_center.setText("体检信息");
        getData(true);
    }

    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll_a1, R.id.ll_a2, R.id.ll_a3, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll1:
                openActivity(KfxtActivity.class, 6);
                break;
            case R.id.ll2:
                openActivity(KfxtActivity.class, 7);
                break;
            case R.id.ll3:
                openActivity(KfxtActivity.class, 8);
                break;
            case R.id.ll4:
                openActivity(KfxtActivity.class, 9);
                break;
            case R.id.ll5:
                openActivity(KfxtActivity.class, 10);
                break;
            case R.id.ll6:
                openActivity(NewActivity.class,1);
                break;
            case R.id.ll_a1:
                openActivity(JbsActivity.class);
                break;
            case R.id.ll_a2:
                show_fy();
                break;
            case R.id.ll_a3:
                openActivity(WdsbActivity.class);
                break;
        }
    }

    void show_fy() {
        list_fy_select.clear();;
        list_fy_select.addAll(list_fy);
        isUpdate = false;
        if (dialog_fy == null) {
            dialog_fy = new Dialog(this, R.style.Dialog);
            dialog_fy.setContentView(R.layout.dialog_tv_n);
            TextView tv_title_tv = (TextView) dialog_fy.findViewById(R.id.tv_title);
            tv_title_tv.setText("服药史");
            dialog_fy.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isUpdate) {
                        dialog_fy.dismiss();
                        return;
                    }
                    String batch_id = "";

                    for (int i = 0; i < list_fy.size(); i++) {
                        if (list_fy.get(i).getIs_select() != null && list_fy.get(i).getIs_select().equals("1")) {
                            if (batch_id.equals("")) {
                                batch_id += "" + list_fy.get(i).getTitle();
                            } else {
                                batch_id += "," + list_fy.get(i).getTitle();
                            }
                            list_delete.add(list_fy.get(i));
                        }
                    }
                    AsyncHttpParams param = new AsyncHttpParams();
                    param.put("user_id", App.getUser().getId());
                    param.put("batch_name", batch_id);
                    progress("修改中...");
                    AsyncHttpRequest.post(Api.fys_update_and_insert, param, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                tagAdapter.notifyDataChanged();
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                    dialog_fy.dismiss();
                }
            });

            dialog_fy.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_fy.dismiss();
                }
            });
            System.out.println(list_fy.size());
            TagFlowLayout fl = (TagFlowLayout) dialog_fy.findViewById(R.id.fl);
            tagAdapter = new TagAdapter<Allergy>(list_fy_select) {
                @Override
                public View getView(FlowLayout parent, int position, Allergy allergy) {
                    isUpdate = true;
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                    if (allergy.getIs_select() != null && allergy.getIs_select().equals("1")) {
                        textView.setBackgroundResource(R.drawable.shape_round_blue);
                        textView.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                        textView.setTextColor(getResources().getColor(R.color.black));
                    }
                    textView.setText(allergy.getTitle());
                    return textView;
                }
            };
            fl.setAdapter(tagAdapter);
            fl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
//                    toask(list_jj.get(position).getName());
                    if (list_fy.get(position).getIs_select() != null && list_fy.get(position).getIs_select().equals("1")) {
                        list_fy.get(position).setIs_select("0");
                    } else {
                        list_fy.get(position).setIs_select("1");
                    }
                    tagAdapter.notifyDataChanged();
                    return true;
                }
            });
            dialog_fy.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    int count = 0;
                    for (int i = 0; i < list_fy.size(); i++) {
                        if (list_fy.get(i).getIs_select()!=null && list_fy.get(i).getIs_select().equals("1")) count++;
                    }
                    if (count < 1) {
                        tv_a2.setVisibility(View.GONE);
                        iv_a2.setVisibility(View.VISIBLE);
                    } else {
                        iv_a2.setVisibility(View.GONE);
                        tv_a2.setVisibility(View.VISIBLE);
                        tv_a2.setText(count + "");
                    }
                }
            });
        }
        dialog_fy.show();
    }

    public void getData(final boolean b) {
        if(b)progress("加载中...");
        requestParams.put("user_id", App.getUser().getId());
        AsyncHttpRequest.post(Api.tjxx_all_info, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                t = fromJson(bytes, Tjxx.class);
                if (t.isSuccess()) {
                    init();
                    getFysData(b);
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

    private void getFysData(final boolean b) {
        if(b) load();
        AsyncHttpParams requestParams = AsyncHttpParams.New();
        requestParams.put("user_id",App.getUser().getId());
        AsyncHttpRequest.post(Api.fys_all_and_user, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    if(b)load_succeed();
                    Fys d = fromJson(bytes, Fys.class);
                    list_fy.clear();
                    list_fy.addAll(d.getData());
//                    if (tagAdapter == null) {
//                        init
//                    }
//                    tagAdapter.notifyDataChanged();
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

    private void init() {
        tv1.setText(t.data.getKfxt());
        tv2.setText(t.data.getZdgc());
        tv3.setText(t.data.getGysz());
        tv4.setText(t.data.getGmdzdb());
        tv5.setText(t.data.getThxhdb());
        if (t.data.getJbs_count().equals("0")) {
            tv_a1.setVisibility(View.GONE);
            iv_a1.setVisibility(View.VISIBLE);
        } else {
            tv_a1.setVisibility(View.VISIBLE);
            tv_a1.setText(t.data.getJbs_count());
            iv_a1.setVisibility(View.GONE);
        }
        if (t.data.getFys_count().equals("0")) {
            tv_a2.setVisibility(View.GONE);
            iv_a2.setVisibility(View.VISIBLE);
        } else {
            tv_a2.setVisibility(View.VISIBLE);
            tv_a2.setText(t.data.getFys_count());
            iv_a2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        getData(false);
        super.onRestart();
    }
}
