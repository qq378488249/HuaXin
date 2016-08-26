package cc.chenghong.huaxin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.entity.Allergy;

public class EditActivity extends BaseActivity {
    /**
     * code码
     * 1 姓名
     */
    private int code;
    private String value;
    private int id;

    @ViewInject(R.id.et)
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_edit);
        initXutils();
        code = getIntent().getIntExtra("code", 0);
        value = getIntent().getStringExtra("value");
        id = getIntent().getIntExtra("id",0);
        if (value == null || value.equals("")) {

        } else {
            et.setText(value);
        }
        switch (code) {
            case 0:

                break;
            case 1:
                setTitleName("姓名");
                et.setHint("请输入您的名字");
                break;
            case 2:
                setTitleName("紧急联系人电话");
                et.setHint("请输入您的紧急联系人电话");
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 3:
                setTitleName("会员卡");
                et.setHint("请输入您的会员卡号");
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 4:
                setTitleName("自定义过敏史");
                et.setHint("请输入自定义过敏史");
                break;
        }
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
//        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(et, 0);
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
// 接受软键盘输入的编辑文本或其它视图
//        imm.showSoftInput(submitBt,InputMethodManager.SHOW_FORCED);
    }

    @OnClick(R.id.tv)
    private void onclick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                switch (code) {
                    case 0:

                        break;
                    case 1:
                        Intent intent = new Intent(EditActivity.this, RecordActivity.class);
                        intent.putExtra("value", et.getText().toString());
                        setResult(code, intent);
                        finish();
                        break;
                    case 2:
                        String value = et.getText().toString();
                        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
                        Pattern p = Pattern.compile(regExp);
                        Matcher m = p.matcher(value);
                        if (m.find()) {
                            Intent intent1 = new Intent(EditActivity.this, RecordActivity.class);
                            intent1.putExtra("value", et.getText().toString());
                            setResult(code, intent1);
                            finish();
                        }else{
                            App.toask("请输入正确的手机号码");
                        }
                        break;
                    case 3:
                        if(et.getText().toString().equals("")){
                            App.toask("请输入会员卡号");
                        }else{
                            if(viewGetValue(et).length()> 10 ){
                                App.toask("请检查您的卡号");
                                return;
                            }
                            Intent intent1 = new Intent(EditActivity.this, RecordActivity.class);
                            intent1.putExtra("value", et.getText().toString());
                            setResult(code, intent1);
                            finish();
                        }
                        break;
                    case 4:
                        Intent intent1 = new Intent(EditActivity.this, RecordActivity.class);
                        id++;
                        Allergy a = new Allergy(id,et.getText().toString(),true);
                        intent1.putExtra("value", a);
                        setResult(code, intent1);
                        finish();
                        break;
                }
                break;
        }
    }

}
