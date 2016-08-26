package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.User;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import de.greenrobot.event.EventBus;

/**
 * 修改姓名
 */

public class XgxmActivity extends BaseActivity {
    @ViewInject(R.id.tv)
    TextView tv;
    @ViewInject(R.id.tva)
    TextView tva;
    @ViewInject(R.id.et)
    EditText et;

    User user;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_xgxm);
        initXutils();
        openInput(et);
        user = App.getUser();
        tv_rigth.setText("保存");
        tv_rigth.setVisibility(View.VISIBLE);
        if (code == 1) {
            setTitleName("个人信息");
            if (stringIsNull(user.getNick_name())) {
                et.setText("");
            } else {
                et.setText(user.getNick_name());
                et.setSelection(user.getNick_name().length());
            }
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            tv_rigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewIsNull(et)) {
                        toask("请输入姓名");
                        return;
                    }
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("id", App.getUser().getId());
                    requestParams.put("nick_name", viewGetValue(et));
                    progress("修改中...");
                    client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                user.setNick_name(viewGetValue(et));
                                App.setUser(user);
                                Intent inten = new Intent(XgxmActivity.this, GrxxActivity.class);
                                inten.putExtra("data", viewGetValue(et));
                                setResult(RESULT_OK, inten);
                                finish();
                            } else {
                                toask("修改成功");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                }
            });
        }
        if (code == 4) {
            setTitleName("个人信息");
            tv.setText("紧急联系人电话");
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (user.getUrgent_mobile() == null) {
                et.setText("");
            } else {
                et.setText(user.getUrgent_mobile() + "");
                et.setSelection(user.getUrgent_mobile().length());
            }
            et.setHint("请输入紧急联系人电话");
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            tv_rigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewIsNull(et)) {
                        toask("请输入紧急联系人电话");
                        return;
                    }
                    boolean b = isPhoneNum(et);
                    System.out.println(b);
                    if (!isPhoneNum(et)) {
                        toask("请输入正确的电话号码");
                        return;
                    }
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("id", App.getUser().getId());
                    requestParams.put("urgent_mobile", viewGetValue(et));
                    progress("修改中...");
                    client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                user.setUrgent_mobile(viewGetValue(et));
                                App.setUser(user);
                                Intent inten = new Intent(XgxmActivity.this, GrxxActivity.class);
                                inten.putExtra("data", viewGetValue(et));
                                setResult(RESULT_OK, inten);
                                finish();
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
            });
        }
        if (code == 5) {//添加药物过敏史
            setTitleName("药物过敏史");
            et.setText("");
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            tv.setText("自定义药物过敏");
            et.setHint("请输入自定义药物过敏");
            tv_rigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewIsNull(et)) {
                        toask("请输入过敏药物");
                        return;
                    }
                    requestParams.put("user_id", App.getUser().getId());
                    requestParams.put("title", viewGetValue(et));
                    progress("添加中...");
                    AsyncHttpRequest.post(Api.ywgms_custom_insert, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            if (i == 200) {
                                new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        hideProgress();
                                        toask("添加成功");
                                        finish();
                                    }
                                }.sendMessageDelayed(Message.obtain(), 3000);
                            } else {
                                hideProgress();
                                toask("添加失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                }
            });
        }
        if (code == 6 || code == 7 || code == 8 || code == 9 || code == 10) {//空腹血糖
            final String add_url = getIntent().getStringExtra("add_url");
            //最大值
            int max = 0;
            final double min = 0.01;
            switch (code) {
                case 6:
                    str = "空腹血糖";
                    max = 30;
                    break;
                case 7:
                    str = "总胆固醇";
                    max = 15;
                    break;
                case 8:
                    str = "甘油三脂";
                    max = 6;
                    break;
                case 9:
                    str = "高密度脂蛋白";
                    max = 6;
                    break;
                case 10:
                    str = "糖化血红蛋白";
                    tva.setText("%");
                    max = 21;
                    break;
            }
            setTitleName(str);
            tva.setVisibility(View.VISIBLE);
            et.setText("");
            et.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
            tv.setText(str);
            et.setHint("请输入" + str + "数据");
            final int finalMax = max;
            tv_rigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewIsNull(et)) {
                        toask("请输入" + str + "数据");
                        return;
                    }
                    if (!checkMoney(et)) {
                        toask("输入有误，最多只能输入2位小数");
                        return;
                    }
                    if (code != 10) {
                        if (viewValueFormatDouble(et) > finalMax) {
                            toask(str + "范围：" + min + "-" + finalMax);
                            return;
                        }
                    } else {
//                        String value = viewGetValue(et).substring(0, viewGetValue(et).length() - 1);
//                        double d = Double.valueOf(value);
                        if (viewValueFormatDouble(et) > finalMax) {
                            toask(str + "范围：" + min + "-" + finalMax + "%");
                            return;
                        }
                    }
                    progress("添加中...");
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", App.getUser().getId());
                    requestParams.put("value", stringToDouble(et));
                    client().post(add_url, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("添加成功");
                                setResult(RESULT_OK, new Intent(XgxmActivity.this, KfxtActivity.class));
                                finish();
                            } else {
                                toask("添加失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                }
            });
        }

        if (code == 11) {//修改会员卡
            final String str = "会员卡号";
            setTitleName("个人信息");
            tv.setText(str);
            et.setHint("请输入" + str);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            if (user.getCard() == null) {
                et.setText("");
            } else {
                et.setText(user.getCard());
                et.setSelection(viewGetValue(et).length());
            }
            tv_rigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewIsNull(et)) {
                        toask("请输入" + str);
                        return;
                    }
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("id", App.getUser().getId());
                    requestParams.put("card", viewGetValue(et));
                    progress("修改中...");
                    client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                user.setCard(viewGetValue(et));
                                App.setUser(user);
                                finish();
                            } else {
                                toask("修改成功");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                }
            });
        }

        if (code == 12) {
            final String str = "自定义药品名称";
            setTitleName("药品名称");
            tv.setText(str);
            et.setHint("请输入" + str);
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            tv_rigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewIsNull(et) || viewGetValue(et).trim().equals("")) {
                        toask("请输入" + str);
                        return;
                    }
                    EventBus.getDefault().post(viewGetValue(et).trim());
                    finish();
                }
            });
        }
    }
}
