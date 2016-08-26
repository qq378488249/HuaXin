package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.DialogInterface;
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

import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Allergy;
import cc.chenghong.huaxin.entity.Jbs;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.FlowLayout;
import cc.chenghong.huaxin.view.TagAdapter;
import cc.chenghong.huaxin.view.TagFlowLayout;

/**
 * 疾病史 20160324
 */
public class JbsActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;
    TextView tv;

    List<Jbs> list = new ArrayList<Jbs>();
    CommonAdapter<Jbs> adapter;

    TagFlowLayout fl;
    TagAdapter<Allergy> fl_adapter;

    List<Allergy> list0 = new ArrayList<Allergy>();
    List<Allergy> list1 = new ArrayList<Allergy>();
    List<Allergy> list2 = new ArrayList<Allergy>();
    List<Allergy> list3 = new ArrayList<Allergy>();
    List<Allergy> list4 = new ArrayList<Allergy>();
    List<Allergy> list5 = new ArrayList<Allergy>();
    List<Allergy> list_delete = new ArrayList<Allergy>();
    List<Allergy> list_select = new ArrayList<Allergy>();
    Dialog dialog;
    //是否修改过
    boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_jbs);
        setTitleName("疾病史");
        initXutils();
        getData();
    }

    private void getData() {
        load();
        AsyncHttpRequest.post(Api.jbs_all_and_user, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    load_succeed();
                    String s = new String(bytes);
                    System.out.println(s);
                    Jbs j = fromJson(bytes, Jbs.class);
                    list.clear();
                    list.addAll(j.data);
                    if (adapter == null) {
                        initAdapter();
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
        adapter = new CommonAdapter<Jbs>(this, list, R.layout.lv_item_jbs) {
            @Override
            public void convert(ViewHolder helper, Jbs item, int position) {
                helper.setText(R.id.tv1, item.getTitle());
                ImageView iv = helper.getView(R.id.iv);
                TextView tv = helper.getView(R.id.tv2);
                if (item.getUser_select_count() == 0) {
                    tv.setVisibility(View.GONE);
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(item.getUser_select_count() + "");
                }
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                show(list.get(i).getSon_data(), list.get(i).getTitle(), i);
            }
        });
    }

    void show(final List<Allergy> list1, String title , final int index ) {
        if (list1.size() == 0) {
            toask("该类别暂无数据");
            return;
        }
        list_select.clear();
        for (Allergy allergy : list1) {
            Allergy a = new Allergy(allergy.getIs_select(),allergy.getTitle());
            list_select.add(a);
        }
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_tv_n);
            tv = (TextView) dialog.findViewById(R.id.tv_title);
            fl = (TagFlowLayout) dialog.findViewById(R.id.fl);
            dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isUpdate) {
                        dialog.dismiss();
                        return;
                    }
                    String batch_id = "";
                    for (int i = 0; i < list_select.size(); i++) {

                        if (list_select.get(i).getIs_select() != null && list_select.get(i).getIs_select().equals("1")) {
                            if (batch_id.equals("")) {
                                batch_id += "" + list_select.get(i).getTitle();
                            } else {
                                batch_id += "," + list_select.get(i).getTitle();
                            }
//                            list_delete.add(list_select.get(i));
                        }
                    }
                    requestParams.put("batch_name", batch_id);
                    submit(index, batch_id);
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
        tv.setText(title);
        fl_adapter = new TagAdapter<Allergy>(list_select) {
            @Override
            public View getView(FlowLayout parent, int position, Allergy allergy) {
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
//                    textView.setBackgroundResource(R.drawable.selector_record);
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
        fl.setAdapter(fl_adapter);
        fl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                isUpdate = true;
                if (list_select.get(position).getIs_select() != null && list_select.get(position).getIs_select().equals("1")) {
                    list_select.get(position).setIs_select("0");
                } else {
                    list_select.get(position).setIs_select("1");
                }
                fl_adapter.notifyDataChanged();
                return true;
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    void submit(final int index, final String str) {
        progress("修改中...");
        AsyncHttpRequest.post(Api.jbs_update_and_insert, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    toask("修改成功");
                    if (str.equals("")) {
                        list.get(index).setUser_select_count(0);
                    } else {
                        String s[] = str.split(",");
                        list.get(index).setUser_select_count(s.length);
                    }
                    list.get(index).getSon_data().clear();
                    list.get(index).getSon_data().addAll(list_select);
//                    list.get(index).setSon_data(list_select);
//                    list1.clear();
//                    list1.addAll(list_select);
                    adapter.notifyDataSetChanged();
                } else {
                    toask("修改失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }
}
