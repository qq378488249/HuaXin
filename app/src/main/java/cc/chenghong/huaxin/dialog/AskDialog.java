package cc.chenghong.huaxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cc.chenghong.huaxin.activity.R;

/**
 * Created by hcl on 2016/6/13.
 */
public class AskDialog extends Dialog {
    final static String TAG = AskDialog.class.getSimpleName();
    View ll;
    TextView tvTitle;
    TextView tvCotent;
    TextView tvYes;
    TextView tvNo;
    String strTitle = "";
    String strContent = "";
    String strYes = "确定";
    String strNo = "取消";

    View.OnClickListener yesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    View.OnClickListener noOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public AskDialog(Context context) {
        super(context,R.style.Dialog);
//        getWindow().getAttributes().windowAnimations = R.style.SlideInLeftDialogAnimation;//设置进入和退出动画
//
//        WindowManager.LayoutParams wl = getWindow().getAttributes();
//        wl.gravity = Gravity.CENTER;//居中显示
//        getWindow().setAttributes(wl);
//
//        setCanceledOnTouchOutside(true);//设置点击外部空白区域关闭弹窗
    }
    public AskDialog(Context context,String title,String content) {
        super(context,R.style.Dialog);
        strTitle = title;
        strContent = content;
//        getWindow().getAttributes().windowAnimations = R.style.SlideInLeftDialogAnimation;//设置进入和退出动画
//
//        WindowManager.LayoutParams wl = getWindow().getAttributes();
//        wl.gravity = Gravity.CENTER;//居中显示
//        getWindow().setAttributes(wl);
//
//        setCanceledOnTouchOutside(true);//设置点击外部空白区域关闭弹窗
    }

    public AskDialog(Context context, int theme) {
        super(context, theme);
    }

    protected AskDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_ask);
        //设置视图宽度为屏幕宽度
        ll = findViewById(R.id.ll);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ll.getLayoutParams();
//        lp.width = getWindow().getWindowManager().getDefaultDisplay().getWidth() - 40;
        lp.width = getContext().getResources().getDisplayMetrics().widthPixels - 40;
        lp.leftMargin = 20;
        lp.rightMargin = 20;
        ll.setLayoutParams(lp);


        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCotent = (TextView) findViewById(R.id.tv_content);
        tvYes = (TextView) findViewById(R.id.tv_yes);
        tvNo = (TextView) findViewById(R.id.tv_no);

        tvTitle.setText(strTitle);
        tvCotent.setText(strContent);
        tvYes.setText(strYes);
        tvNo.setText(strNo);

        tvYes.setOnClickListener(yesOnClickListener);
        tvNo.setOnClickListener(noOnClickListener);

//		btn_cancel = (Button) findViewById(R.id.btn_cancel);
//		btn_1 = (Button) findViewById(R.id.btn_1);
//		btn_2 = (Button) findViewById(R.id.btn_2);
//        ll_left = (LinearLayout) findViewById(R.id.ll_left);
//        ll_rigth = (LinearLayout) findViewById(R.id.ll_rigth);
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则返回null
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = findViewById(viewId);
        if (view == null) {
//            view = mConvertView.findViewById(viewId);
//            mViews.put(viewId, view);
            return null;
        }
        return (T) view;
    }

    public AskDialog setTvTitle(String str) {
        strTitle = str;
//        if (tvTitle!=null){
//            tvTitle.setText(str+"");
//        }
        return this;
    }

    public AskDialog setTvContent(String str) {
//        if (tvCotent!=null){
//            tvCotent.setText(str+"");
//        }
        strContent = str;
        return this;
    }
    public AskDialog setTvYes(String str) {
//        if (tvYes!=null){
//            tvYes.setText(str+"");
//        }
        strYes =str;
        return this;
    }
    public AskDialog setTvNo(String str) {
//        if (tvNo!=null){
//            tvNo.setText(str+"");
//        }
        strNo = str;
        return this;
    }

    public AskDialog setTvYesOnClickListener(View.OnClickListener listener){
        if (listener != null) {
            yesOnClickListener = listener;
        }
        return this;
    }
    public AskDialog setTvNoOnClickListener(View.OnClickListener listener){
        if (listener != null) {
            noOnClickListener = listener;
        }
        return this;
    }

}
