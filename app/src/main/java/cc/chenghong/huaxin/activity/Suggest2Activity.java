package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.ViewPaperAdapter;
import cc.chenghong.huaxin.adapter.ZoomOutPageTransformer;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Page;
import cc.chenghong.huaxin.entity.Suggest2;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;

/**
 * 促销信息
 */
public class Suggest2Activity extends BaseActivity {
    @ViewInject(R.id.vp)
    ViewPager vp;
    @ViewInject(R.id.ll_point)
    LinearLayout ll_point;

    ArrayList<View> list_views = new ArrayList<View>();
    ArrayList<Suggest2> list = new ArrayList<Suggest2>();
    ViewPaperAdapter adapter;

    Page<Suggest2> page = new Page<Suggest2>(0,10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_suggest2);
        initXutils();
        setTitleName("促销信息");
        getData();
    }

    private void initVp() {

        for (int i = 0; i < list.size(); i++) {
            View view = View.inflate(this, R.layout.vp_suggest2, null);
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
            ViewGroup.LayoutParams lp = ll.getLayoutParams();
            lp.height = (int) (getHeight() * 0.618);
            ll.setLayoutParams(lp);

            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            getUrlBitmap(list.get(i).getImg_url(), iv);
            final int a = i;
            view.findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vp.getCurrentItem() == a) {
                        startActivity(new Intent(Suggest2Activity.this, Suggest2ContentActivity.class).putExtra("data",list.get(a).getId()));
                    }
                    vp.setCurrentItem(a);
                }
            });
            TextView tv1 = (TextView) view.findViewById(R.id.tv1);
            tv1.setText(list.get(i).getTitle());
            TextView tv2 = (TextView) view.findViewById(R.id.tv2);
            tv2.setText(list.get(i).getSmall_titile());
            list_views.add(view);
        }

        initPoint();
        set_select(0);
        adapter = new ViewPaperAdapter(list_views);
        vp.setOffscreenPageLimit(list_views.size());
        vp.setPageMargin(-dip2px(100));
//        vp.setPageMargin(-dip2px(80));
        vp.setAdapter(adapter);
        vp.setPageTransformer(true, new ZoomOutPageTransformer());
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                set_select(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        for (int i = 0; i < list_views.size(); i++) {
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.tv_point, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
            lp.setMargins(0, 0, 10, 0);
            tv.setLayoutParams(lp);
            ll_point.addView(tv);
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    void set_select(int index) {
        for (int i1 = 0; i1 < ll_point.getChildCount(); i1++) {
            ll_point.getChildAt(i1).setSelected(false);
        }
        for (int i1 = 0; i1 < ll_point.getChildCount(); i1++) {
            if (i1 == index) {
                ll_point.getChildAt(i1).setSelected(true);
            }
        }
    }

    public void getData() {
        progress("加载中...");
        AsyncHttpParams requestParams = new AsyncHttpParams();
        requestParams.put("user_id", App.getUser().getId());
        requestParams.put("page_size", page.page_size);
        requestParams.put("page_index", page.page_index);
        load();
        AsyncHttpRequest.post(Api.cxxx_page, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    Suggest2 s = fromJson(bytes, Suggest2.class);
                    if (s.data.size() == 0) {
                        toask("暂无数据");
                    }
                    list.clear();
                    list.addAll(s.data);
                    initVp();
                } else {
                    load_fail();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network();
            }
        });
    }

}
