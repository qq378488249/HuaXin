package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Ywgms;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.FlowLayout;
import cc.chenghong.huaxin.view.TagAdapter;
import cc.chenghong.huaxin.view.TagFlowLayout;

/**
 * 用药信息
 */
public class YyxxActivity extends BaseActivity {

    Dialog dialog_gm;//药物过敏
    Dialog dialog_jj;//用药禁忌

    TagFlowLayout fl_gm;//药物过敏流布局
    TagAdapter<Ywgms> adapter_gm;//药物过敏适配器
    TagFlowLayout fl_jj;//用药禁忌流布局
    TagAdapter<Ywgms> adapter_jj;//用药禁忌适配器

    List<Ywgms> list_gm = new ArrayList<Ywgms>();
    List<Ywgms> list_jj = new ArrayList<Ywgms>();

    Ywgms gm;
    Ywgms jj;
    //是否操作过
    boolean isCom = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_yyxx);
        initXutils();
        setTitleName("用药信息");
        getData(true);
    }

    private void getData(final boolean b) {
        requestParams.put("user_id", App.getUser().getId());
        if(b)progress("加载中...");
        AsyncHttpRequest.post(Api.ywgms_all_and_user, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {
                    gm = fromJson(bytes, Ywgms.class);
                    list_gm.clear();
                    list_gm.addAll(gm.data);
                    if (b) getData1(b);
                } else {
                    if (b) load_fail();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void getData1(final boolean b) {
        if(b)load();
        AsyncHttpRequest.post(Api.yyjjz_all_and_user, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
//                if(b)load_succeed();
                if (i == 200) {
                    jj = fromJson(bytes, Ywgms.class);
                    list_jj.clear();
                    list_jj.addAll(jj.data);
                    if (adapter_gm != null) {
                        adapter_gm.notifyDataChanged();
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

    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                openActivity(NewActivity.class,2);
                break;
            case R.id.ll2:
                show_gm();
                break;
            case R.id.ll3:
                show_jj();
                break;
        }
    }

    private void show_jj() {
        isCom = false;
        list_select.clear();
        for (Ywgms ywgms : list_jj) {
            Ywgms y = new Ywgms();
            y.setIs_select(ywgms.getIs_select());
            y.setTitle(ywgms.getTitle());
            list_select.add(y);
        }
        if (dialog_jj == null) {
            dialog_jj = new Dialog(this, R.style.Dialog);
            dialog_jj.setContentView(R.layout.dialog_tv_n);
            TextView tv_title_tv = (TextView) dialog_jj.findViewById(R.id.tv_title);
            tv_title_tv.setText("用药禁忌症");

            dialog_jj.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isCom) {//未进行任何操作
                        dialog_jj.dismiss();
                        return;
                    }
                    String s = "";
                    for (Ywgms ywgms : list_select) {
                        if (ywgms != null && ywgms.getIs_select().equals("1")) {
                            if (s.equals("")) {
                                s += ywgms.getTitle();
                            } else {
                                s += "," + ywgms.getTitle();
                            }
                        }
                    }
                    AsyncHttpParams requestParams = AsyncHttpParams.New();
                    requestParams.put("batch_name", s);
                    requestParams.put("user_id", App.getUser().getId());
                    progress("修改中...");
                    AsyncHttpRequest.post(Api.yyjjz_update_and_insert, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                dialog_jj.dismiss();
                                list_jj.clear();
                                list_jj.addAll(list_select);
//                                getData(false);
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                    dialog_jj.dismiss();
                }
            });

            dialog_jj.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_jj.dismiss();
                }
            });

            fl_jj = (TagFlowLayout) dialog_jj.findViewById(R.id.fl);
            fl_jj.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    isCom = true;
                    if (list_select.get(position) != null && list_select.get(position).getIs_select().equals("1")) {
                        list_select.get(position).setIs_select("0");
                    } else {
                        list_select.get(position).setIs_select("1");
                    }
                    adapter_jj.notifyDataChanged();
                    return true;
                }
            });
        }
        adapter_jj = new TagAdapter<Ywgms>(list_select) {
            @Override
            public View getView(FlowLayout parent, int position, Ywgms Ywgms) {
                isCom = true;
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                textView.setBackgroundResource(R.drawable.selector_record);
                if (Ywgms.getIs_select().equals("1")) {
//                        textView.setSelected(true);
                    textView.setBackgroundResource(R.drawable.shape_round_blue);
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
//                        textView.setSelected(false);
                    textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                textView.setText(Ywgms.getTitle());
                return textView;
            }
        };
        fl_jj.setAdapter(adapter_jj);
        dialog_jj.show();
    }
    List<Ywgms> list_select = new ArrayList<>();
    private void show_gm() {
        list_select.clear();
        for (Ywgms ywgms : list_gm) {
            Ywgms y = new Ywgms();
            y.setIs_select(ywgms.getIs_select());
            y.setTitle(ywgms.getTitle());
            list_select.add(y);
        }
        if (dialog_gm == null) {
            dialog_gm = new Dialog(this, R.style.Dialog);
            dialog_gm.setContentView(R.layout.dialog_tv_n);
            TextView tv_title_tv = (TextView) dialog_gm.findViewById(R.id.tv_title);
            tv_title_tv.setText("药物过敏史");
            LinearLayout ll = (LinearLayout) dialog_gm.findViewById(R.id.ll_add);
            ll.setVisibility(View.VISIBLE);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(YyxxActivity.this, XgxmActivity.class);
                    intent.putExtra("code", 5);
                    startActivityForResult(intent, 1);
                    dialog_gm.dismiss();
                }
            });

            dialog_gm.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isCom) {//未进行任何操作
                        dialog_gm.dismiss();
                        return;
                    }
                    String s = "";
                    for (Ywgms ywgms : list_select) {
                        if (ywgms.getIs_select().equals("1")) {
                            if (s.equals("")) {
                                s += ywgms.getTitle();
                            } else {
                                s += "," + ywgms.getTitle();
                            }
                        }
                    }
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("batch_name", s);
                    requestParams.put("user_id", App.getUser().getId());
                    progress("修改中...");
                    client().post(Api.ywgms_update_and_insert, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                dialog_gm.dismiss();
                                getData(false);
//                                list_gm.clear();
//                                list_gm.addAll(list_select);
//                                getData(false);
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            hideProgress();
                            toask("修改失败");
                        }
                    });
                    dialog_gm.dismiss();
                }
            });

            dialog_gm.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_gm.dismiss();
                }
            });

            fl_gm = (TagFlowLayout) dialog_gm.findViewById(R.id.fl);
            fl_gm.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    isCom = true;
                    if (list_select.get(position).getIs_select().equals("1")) {
                        list_select.get(position).setIs_select("0");
                    } else {
                        list_select.get(position).setIs_select("1");
                    }
                    adapter_gm.notifyDataChanged();
                    return true;
                }
            });
        }
        adapter_gm = new TagAdapter<Ywgms>(list_select) {
            @Override
            public View getView(FlowLayout parent, int position, Ywgms Ywgms) {
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                textView.setBackgroundResource(R.drawable.selector_record);
                if (Ywgms.getIs_select().equals("1")) {
//                        textView.setSelected(true);
                    textView.setBackgroundResource(R.drawable.shape_round_blue);
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
//                        textView.setSelected(false);
                    textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                textView.setText(Ywgms.getTitle());
                if (Ywgms.getTitle().equals("")) {
                    textView.setVisibility(View.GONE);
                }
                return textView;
            }
        };
        fl_gm.setAdapter(adapter_gm);
        dialog_gm.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Ywgms a = (Ywgms) mIntent.getSerializableExtra("data");
            list_gm.add(a);
            adapter_gm.notifyDataChanged();
        }
        super.onActivityResult(requestCode, resultCode, mIntent);
    }

    @Override
    protected void onRestart() {
        getData(false);
        super.onRestart();
    }
}
