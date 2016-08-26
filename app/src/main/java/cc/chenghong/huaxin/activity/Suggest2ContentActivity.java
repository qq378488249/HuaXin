package cc.chenghong.huaxin.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Cxxq;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;

/**
 * 促销详情
 */
public class Suggest2ContentActivity extends BaseActivity {
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.wv)
    WebView wv;
    @ViewInject(R.id.sv)
    ScrollView sv;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.tv_title2)
    TextView tv_title2;
    @ViewInject(R.id.tv_count)
    TextView tv_count;
    @ViewInject(R.id.tv_content)
    TextView tv_content;
    @ViewInject(R.id.tvBottom)
    TextView tvBottom;

    Cxxq cxxq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_suggest2_content);
        initXutils();
        setTitleName("促销详情");
        getData();
        setViewWidth(sv, getWidth());
//        init();
    }

    private void getData() {
        load();
        AsyncHttpParams asyncHttpParams = AsyncHttpParams.New();
        asyncHttpParams.put("id", getIntent().getStringExtra("data"));
        asyncHttpParams.put("user_id", App.getUser().getId());
        AsyncHttpRequest.post(Api.cxxx_get, asyncHttpParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    cxxq = fromJson(bytes, Cxxq.class);
                    cxxq = cxxq.data;
                    init();
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
        if (cxxq != null) {
            getUrlBitmap(cxxq.getImg_url(), iv);
            wv.getSettings().setDefaultTextEncodingName("UTF-8");
            setViewWidth(wv, getWidth());

            WebSettings webSettings = wv.getSettings();

// User settings

            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);//关键点

            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            webSettings.setDisplayZoomControls(false);
            webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
            webSettings.setAllowFileAccess(true); // 允许访问文件
            webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
            webSettings.setSupportZoom(true); // 支持缩放

            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDefaultFontSize(20);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int mDensity = metrics.densityDpi;
            Log.d("maomao", "densityDpi = " + mDensity);
            if (mDensity == 240) {
                webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
            } else if (mDensity == 160) {
                webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            } else if(mDensity == 120) {
                webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
            }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
                webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
            }else if (mDensity == DisplayMetrics.DENSITY_TV){
                webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
            }else{
                webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            }


/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
 */
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

//            int screenDensity = getResources().getDisplayMetrics().densityDpi ;
//            WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM ;
//            switch (screenDensity){
//                case DisplayMetrics.DENSITY_LOW :
//                    zoomDensity = WebSettings.ZoomDensity.CLOSE;
//                    break;
//                case DisplayMetrics.DENSITY_MEDIUM:
//                    zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//                    break;
//                case DisplayMetrics.DENSITY_HIGH:
//                    zoomDensity = WebSettings.ZoomDensity.FAR;
//                    break ;
//            }
//            WebSettings webSettings= wv.getSettings();
//            webSettings.setDefaultZoom(zoomDensity);

//            WebSettings webSettings= wv.getSettings();
//            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            webSettings.setUseWideViewPort(true);
//            webSettings.setLoadWithOverviewMode(true);

//            WebSettings s = wv.getSettings();
//            s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//            s.setUseWideViewPort(true);
//            s.setSavePassword(false);
//            s.setSaveFormData(false);
//            s.setBlockNetworkLoads(true);
//            wv.set
//            wv.setu
//            wv.setUseWideViewPort(true);
//            wv.setsett
            wv.loadData(cxxq.getContent() + "", "text/html;charset=UTF-8", null);
            setViewHeigth(iv, getHeight() / 3);
            setText(tv_title, cxxq.getTitle());
            setText(tv_title2,cxxq.getCreated());
            setText(tv_content, cxxq.getContent());
            setText(tv_count,cxxq.getView_count());
//            setText(tvBottom, StringUtils.html2Android(cxxq.getContent()));
            if (!stringIsNull(cxxq.getContent())){
                tvBottom.setText(Html.fromHtml(cxxq.getContent()+""));
            }
        }
    }

    void setText(View view,String value){
        TextView tv = (TextView) view;
        if (value == null){
            tv.setText("");
        }else{
            tv.setText(value);
        }
    }

}
