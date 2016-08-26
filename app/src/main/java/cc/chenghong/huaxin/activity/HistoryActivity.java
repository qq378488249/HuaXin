package cc.chenghong.huaxin.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.HistoryCount;
import cc.chenghong.huaxin.entity.XtContent;
import cc.chenghong.huaxin.entity.XyContent;
import cc.chenghong.huaxin.entity.XyData;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.view.RefreshViewPD;

/**
 * 血压、血糖、体脂历史记录
 * 底部listview添加头部版本
 */
public class HistoryActivity extends BaseActivity {
    //    @ViewInject(R.id.vp)
//    ViewParent vp;
    @ViewInject(R.id.tv1)
    TextView tv1;
    @ViewInject(R.id.tv2)
    TextView tv2;
    @ViewInject(R.id.tv3)
    TextView tv3;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.iv_line)
    ImageView iv_line;
    @ViewInject(R.id.ll_bar1)
    LinearLayout ll_bar1;
    @ViewInject(R.id.ll_top)
    LinearLayout ll_top;

    @ViewInject(R.id.vp)
    ViewPager vp;

    //数据list
    List list1 = new ArrayList();//周数据
    List list2 = new ArrayList();//月数据
    List list3 = new ArrayList();//季数据

    CommonAdapter<Object> adapter1;
    CommonAdapter<Object> adapter2;
    CommonAdapter<Object> adapter3;

    Object object1;//周
    Object object2;//月
    Object object3;//季

    List<View> list_view = new ArrayList<View>();

    ListView lv1;//listview
    ListView lv2;
    ListView lv3;

    RefreshViewPD rv1;//上拉下拉控件
    RefreshViewPD rv2;//上拉下拉控件
    RefreshViewPD rv3;//上拉下拉控件

    LineChart lc1;//折线图
    LineChart lc2;
    LineChart lc3;

    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll3;

    ImageView iv_null1;//无图片
    ImageView iv_null2;
    ImageView iv_null3;

    View view1;//viewpage的item
    View view2;
    View view3;

    View headView1;//listview的head
    View headView2;
    View headView3;

    TextView tvCenter1;//中间的文字
    TextView tvCenter2;
    TextView tvCenter3;

    int width1_3;

    AsyncHttpParams params = new AsyncHttpParams();

    int i = 1;

    //是否加载成功
    boolean isSucceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_history1);
        initXutils();
        statusBar(ll_bar1);
        width1_3 = getWidth()/3;
        setViewWidth(iv_line, getWidth() / 3);
        params.put("user_id", App.getUser().getId());
        if (code == 2) {
            tv_center.setText("历史血糖记录");
            ll_top.setBackgroundResource(R.drawable.pic09);
        }
        if (code == 1) {
            tv_center.setText("历史血压记录");
        }
        if (code == 3) {
            tv_center.setText("历史体脂记录");
            ll_top.setBackgroundResource(R.drawable.pic07);
        }
        getData(1,true);
    }

    //设置viewpage
    private void vp() {
        list_view.add(view1);
        list_view.add(view2);
        list_view.add(view3);
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list_view.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(list_view.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(list_view.get(position));
                return list_view.get(position);
            }
        });
        vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv_line.getLayoutParams();
                lp.leftMargin = (int) (positionOffset * width1_3 + position * width1_3);
                iv_line.setLayoutParams(lp);
//                iv_line.setPadding(UITools.px2dip(20), 0, UITools.px2dip(20), 0);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
//                        App.toask(position);
                        break;
                    case 1:
//                        App.toask(position);
                        break;
                    case 2:
//                        App.toask(position);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initVP() {
        view1 = getLayoutInflater().from(this).inflate(R.layout.vp_history, null);
        view2 = getLayoutInflater().from(this).inflate(R.layout.vp_history, null);
        view3 = getLayoutInflater().from(this).inflate(R.layout.vp_history, null);

        headView1 = getLayoutInflater().from(this).inflate(R.layout.lv_head_history, null);
        headView2 = getLayoutInflater().from(this).inflate(R.layout.lv_head_history, null);
        headView3 = getLayoutInflater().from(this).inflate(R.layout.lv_head_history, null);

        lc1 = (LineChart) headView1.findViewById(R.id.lc);
        lc2 = (LineChart) headView2.findViewById(R.id.lc);
        lc3 = (LineChart) headView3.findViewById(R.id.lc);
        iv_null1 = (ImageView) headView1.findViewById(R.id.iv_null);
        iv_null2 = (ImageView) headView2.findViewById(R.id.iv_null);
        iv_null3 = (ImageView) headView3.findViewById(R.id.iv_null);
        tvCenter1 = (TextView) headView1.findViewById(R.id.tv_center);
        tvCenter2 = (TextView) headView2.findViewById(R.id.tv_center);
        tvCenter3 = (TextView) headView3.findViewById(R.id.tv_center);

        lv1 = (ListView) view1.findViewById(R.id.lv);
        lv2 = (ListView) view2.findViewById(R.id.lv);
        lv3 = (ListView) view3.findViewById(R.id.lv);
        rv1 = (RefreshViewPD) view1.findViewById(R.id.rv);
        rv2 = (RefreshViewPD) view2.findViewById(R.id.rv);
        rv3 = (RefreshViewPD) view3.findViewById(R.id.rv);

        lv1.addHeaderView(headView1);
        lv2.addHeaderView(headView2);
        lv3.addHeaderView(headView3);

        initPage(view1, lc1, list1, 1, adapter1, lv1, rv1, tvCenter1);
        initPage(view2, lc2, list2, 2, adapter2, lv2, rv2, tvCenter2);
        initPage(view3, lc3, list3, 3, adapter3, lv3, rv3, tvCenter3);
//
        vp();
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                vp.setCurrentItem(0);
                break;
            case R.id.tv2:
                vp.setCurrentItem(1);
                break;
            case R.id.tv3:
                vp.setCurrentItem(2);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    /**
     * 初始化一个页面
     *
     * @param v     页面
     * @param lc    折线图
     * @param list  数据集合
     * @param index 第几个页面
     */
    private void initPage(View v, LineChart lc, List<Object> list, final int index, CommonAdapter<Object> adapter, ListView lv, RefreshViewPD rv, TextView tv) {

        switch (code) {
            case 1://血压
                ColorTemplate ct = new ColorTemplate();
                ct.addColorsForDataSets(new int[]{R.color.red, R.color.light_blue, R.color.black}, this);
                lc.setColorTemplate(ct);
                //画圈大小
                lc.setCircleSize(2f);
                //设置画圈
                lc.setDrawCircles(false);
                //从零开始
                lc.setStartAtZero(true);
                // 显示各点上的值
                lc.setDrawYValues(false);
                //设置描述文字
                lc.setDescription("华新医疗");
                lc.setLineWidth(2f);
                //设置y轴上共有多少个值点
                lc.setYLabelCount(4);
                // enable value highlighting
                lc.setHighlightEnabled(true);
                // enable touch gestures
                lc.setTouchEnabled(true);
                // enable scaling and dragging
                lc.setDragEnabled(true);
                // if disabled, scaling can be done on x- and y-axis separately
                lc.setPinchZoom(false);
                lc.setDrawCircles(true);
                lc.setVerticalScrollBarEnabled(false);
                lc.setVerticalFadingEdgeEnabled(false);
                XyContent xy = (XyContent) object1;
                switch (index) {
                    case 2:
                        xy = ((XyContent) object2);
                        break;
                    case 3:
                        xy = ((XyContent) object3);
                        break;
                }
                if (xy.data.count.size() < 2) {//如果数据小于2
                    lc.setVisibility(View.GONE);//隐藏折线图，显示无数据的图
                    switch (index) {
                        case 1:
//                            lc1.setVisibility(View.GONE);
                            iv_null1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
//                            lc2.setVisibility(View.GONE);
                            iv_null2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
//                            lc3.setVisibility(View.GONE);
                            iv_null3.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                List<HistoryCount> list_count = new ArrayList<HistoryCount>();
                list_count.addAll(xy.data.count);
                ArrayList<String> xVals = new ArrayList<String>();
//                xVals.add("sadf");
                for (int i = 0; i < list_count.size(); i++) {
//                    xVals.add(list_count.get(i).getDate());
                    xVals.add(" ");
                }
                ArrayList<Double[]> values = new ArrayList<Double[]>();
                for (int z = 0; z < 3; z++) {
                    Double[] vals = new Double[list_count.size()];
//                    for (int i = 0; i < list_count.size(); i++) {
                    for (int i1 = 0; i1 < list_count.size(); i1++) {
                        switch (z) {
                            case 0:
                                vals[i1] = list_count.get(i1).getSsy();
                                break;
                            case 1:
                                vals[i1] = list_count.get(i1).getSzy();
                                break;
                            case 2:
                                vals[i1] = list_count.get(i1).getXl();
                                break;
                        }
                    }
                    values.add(vals);
                }
                ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
                dataSets.addAll(DataSet.makeDataSets(values));
//                ChartData data = new ChartData(xVals);
                ChartData data = new ChartData(xVals, dataSets);
                lc.setData(data);
                break;
            default:
                ColorTemplate ct2 = new ColorTemplate();
                ct2.addDataSetColors(new int[]{R.color.light_blue}, this);
//        lc.setOnChartValueSelectedListener(this);
                lc.setColorTemplate(ct2);

                // if enabled, the chart will always start at zero on the y-axis
                //是否从零开始
                lc.setStartAtZero(true);

                // 显示折线图各点的值
                lc.setDrawYValues(false);

                //折线的宽度
                lc.setLineWidth(2f);
                //画圈大小
                lc.setCircleSize(2f);
                lc.setDrawCircles(true);
                lc.setDragEnabled(true);
                lc.setDrawBorder(false);

                lc.setBorderStyles(new BarLineChartBase.BorderStyle[]{BarLineChartBase.BorderStyle.BOTTOM});

                // no description text
                //设置描述文字
                lc.setDescription("华新医疗");
                //折线为实线
                lc.disableDashedLine();

                //设置y轴格数
                lc.setYLabelCount(6);

                //显示y轴上的值
                lc.setHighlightEnabled(true);

                // enable touch gestures
                //支持触控手势
                lc.setTouchEnabled(true);

                // enable scaling and dragging
                //支持缩放和拖动
                lc.setDragEnabled(true);

                // if disabled, scaling can be done on x- and y-axis separately
//        如果禁用,扩展可以在x轴和y轴分别完成
                lc.setPinchZoom(true);

                lc.setHighlightIndicatorEnabled(false);

                // set the line to be drawn like this "- - - - - -"
                lc.disableDashedLine();

                XyContent xy2 = (XyContent) object1;
                switch (index) {
                    case 2:
                        xy2 = ((XyContent) object2);
                        break;
                    case 3:
                        xy2 = ((XyContent) object3);
                        break;
                }
                if (xy2.data.data.size() < 2) {//如果数据不大于2，无法绘图,2点确定一条直线
                    lc.setVisibility(View.GONE);//隐藏折线图，显示无数据的图
                    switch (index) {
                        case 1:
                            iv_null1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            iv_null2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            iv_null3.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                List<XyData> list_xy2 = new ArrayList<XyData>();
                list_xy2.addAll(xy2.data.data);

                ArrayList<String> xVals2 = new ArrayList<String>();
                for (int i = 0; i < list_xy2.size(); i++) {
                    xVals2.add(" ");
                }

                ArrayList<Entry> yVals = new ArrayList<Entry>();

                for (int i = 0; i < list_xy2.size(); i++) {
                    if (code == 2) {
                        yVals.add(new Entry(list_xy2.get(i).getXt(), i));
                    } else {
                        yVals.add(new Entry(list_xy2.get(i).getTz(), i));
                    }
                }
                // create a dataset and give it a type
                ArrayList<DataSet> dataSets2 = new ArrayList<DataSet>();
                DataSet dataSet;
                if (code == 2) {
                    dataSet = new DataSet(yVals, "血糖");
                } else {
                    dataSet = new DataSet(yVals, "体脂");
                }
                dataSets2.add(dataSet);
                // create a data object with the datasets
                ChartData data2 = new ChartData(xVals2, dataSets2);
                // set data
                lc.setData(data2);
                break;
        }
        tv.setText("您本周共进行了" + list.size() + "次测量");
        switch (index) {
            case 2:
                tv.setText("您本月共进行了" + list.size() + "次测量");
                break;
            case 3:
                tv.setText("您本季共进行了" + list.size() + "次测量");
                break;
        }
        adapter = new CommonAdapter<Object>(this, list, R.layout.lv_item_history) {
            @Override
            public void convert(ViewHolder helper, Object item, int position) {
                TextView tv1d = helper.getView(R.id.tv1d);
                TextView tv2d = helper.getView(R.id.tv2d);
                TextView tv3c = helper.getView(R.id.tv3c);
                ll1 = helper.getView(R.id.ll1);
                ll2 = helper.getView(R.id.ll2);
                ll3 = helper.getView(R.id.ll3);
                XyContent xy = (XyContent) object1;
                if (index == 2) {
                    xy = (XyContent) object2;
                }
                if (index == 3) {
                    xy = (XyContent) object3;
                }
                XyData x = xy.data.data.get(position);
                switch (code) {
                    case 1:
                        set_ll(ll1);
                        xy2String(x.getSsy(), x.getSzy(), tv1d);
                        helper.setText(R.id.tv1a, x.getSsy() + "/" + x.getSzy());
                        helper.setText(R.id.tv1b, x.getXl());
                        helper.setText(R.id.tv1c, x.getCreated().subSequence(5, x.getCreated().length() - 3));
                        break;
                    case 2:
                        set_ll(ll2);
                        xt(x.getXt() + "", x.getTime_name(), tv2d);
                        helper.setText(R.id.tv2a, x.getXt());
                        helper.setText(R.id.tv2b, x.getCreated().subSequence(5, x.getCreated().length() - 3));
                        helper.setText(R.id.tv2c, x.getTime_name());
                        break;
                    case 3:
                        set_ll(ll3);
                        String s = tz(x.getTz() + "");
                        tv3c.setText(s);
                        if (s.equals("偏瘦")) {
                            tv3c.setBackgroundResource(R.drawable.shape_round_low);
                        } else if (s.equals("正常")) {
                            tv3c.setBackgroundResource(R.drawable.shape_round_green);
                        } else {
                            tv3c.setBackgroundResource(R.drawable.shape_round_red);
                        }
                        helper.setText(R.id.tv3a, x.getTz());
                        helper.setText(R.id.tv3b, x.getCreated().subSequence(5, x.getCreated().length() - 3));
                        break;
                }
            }
        };
        lv.setAdapter(adapter);
        rv.openPullUp();
        rv.openPullDown();
        rv.setListViewScrollListener(lv);
        rv.addOnSnapListener(new RefreshViewPD.OnSnapListener() {
            @Override
            public void onSnapToTop(int distance) {

            }

            @Override
            public void onSnapToBottom(int distance) {

            }
        });
    }

    private void set_ll(LinearLayout ll) {
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
    }

    /**
     * 加载数据
     *
     * @param index
     * @param b  是否递归加载
     **/
    public void getData(final int index, final boolean b) {
        String url = "";
        if (code == 1) {
            if (index == 1) {
                url = Api.xy_get_week;
            } else if (index == 2) {
                url = Api.xy_get_month;
            } else {
                url = Api.xy_get_quarter;
            }
        } else if (code == 2) {
            if (index == 1) {
                url = Api.xt_get_week;
            } else if (index == 2) {
                url = Api.xt_get_month;
            } else {
                url = Api.xt_get_quarter;
            }
        } else {
            if (index == 1) {
                url = Api.tz_get_week;
            } else if (index == 2) {
                url = Api.tz_get_month;
            } else {
                url = Api.tz_get_quarter;
            }
        }
        progress("加载中...");
        AsyncHttpRequest.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    XyContent xy = fromJson(bytes, XyContent.class);
                    if (index == 1) {
                        object1 = xy;
                        list1.addAll(xy.data.data);
                    }
                    if (index == 2) {
                        object2 = xy;
                        list2.addAll(xy.data.data);
                    }
                    if (index == 3) {
                        object3 = xy;
                        list3.addAll(xy.data.data);
                    }
                    if (index > 2) {
                        initVP();
                    } else {
                        if (b) getData(index + 1, b);
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

    public void getData() {
        String url = Api.xy_get_week;
        if (code == 2) {
            url = Api.xt_get_week;
        }
        if (code == 3) {
            url = Api.tz_get_week;
        }
//        if (code == 1) {//血压
        progress("加载中...");
        AsyncHttpRequest.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                String s = new String(bytes);
                log_i(s);
                XyContent xy = fromJson(bytes, XyContent.class);
                object1 = xy;
                if (xy.isSuccess()) {
                    list1.addAll(xy.data.data);
                    getData1();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void getData1() {
        String url = Api.xy_get_month;
        if (code == 2) {
            url = Api.xt_get_month;
        }
        if (code == 3) {
            url = Api.tz_get_month;
        }
//        if (code == 1) {//血压
        progress("加载中...");
        AsyncHttpRequest.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                XyContent xy = fromJson(bytes, XyContent.class);
                object2 = xy;
                if (xy.isSuccess()) {
                    list2.addAll(xy.data.data);
                    getData2();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void getData2() {
        String url = Api.xy_get_quarter;
        if (code == 2) {
            url = Api.xt_get_quarter;
        }
        if (code == 3) {
            url = Api.tz_get_quarter;
        }
//        if (code == 1) {//血压
        progress("加载中...");
        AsyncHttpRequest.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                XyContent xy = fromJson(bytes, XyContent.class);
                object3 = xy;
                if (xy.isSuccess()) {
                    list3.addAll(xy.data.data);
                    initVP();
                } else {
                    toask("加载失败");
                }
//                initVP();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    //血压页面1数据
    void xy1() {
        progress("加载中...");
        AsyncHttpRequest.post(Api.xy_get_month, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                XyContent xy = fromJson(bytes, XyContent.class);
                object2 = xy;
                if (xy.isSuccess()) {
                    list2.addAll(xy.data.data);
                    xy2();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    //血压页面2数据
    void xy2() {
        progress("加载中...");
        AsyncHttpRequest.post(Api.xy_get_quarter, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                XyContent xy = fromJson(bytes, XyContent.class);
                object3 = xy;
                if (xy.isSuccess()) {
                    list3.addAll(xy.data.data);
                    initVP();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    //血糖页面1数据
    void xt1() {
        progress("加载中...");
        AsyncHttpRequest.post(Api.xt_get_month, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                XtContent xy = fromJson(bytes, XtContent.class);
                object2 = xy;
                if (xy.isSuccess()) {
                    list2.addAll(xy.data.data);
                    xt2();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    //血糖页面2数据
    private void xt2() {
        progress("加载中...");
        AsyncHttpRequest.post(Api.xy_get_quarter, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                XyContent xy = fromJson(bytes, XyContent.class);
                object3 = xy;
                if (xy.isSuccess()) {
                    list3.addAll(xy.data.data);
                    initVP();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void xy2String(String str1, String str2, TextView tv) {
        String str = ssyCompareSzy(ssy(str1), szy(str2));
        tv.setText(str);
        if (str.equals("偏低")) {
            tv.setBackgroundResource(R.drawable.shape_round_low);
        }
        if (str.equals("正常")) {
            tv.setBackgroundResource(R.drawable.shape_round_green);
        }
        if (str.equals("一级")) {
            tv.setBackgroundResource(R.drawable.shape_round_low);
        }
        if (str.equals("二级")) {
            tv.setBackgroundResource(R.drawable.shape_round_yellow);
        }
        if (str.equals("三级")) {
            tv.setBackgroundResource(R.drawable.shape_round_red);
        }
    }

    private void xt(String str1, String str2, TextView tv) {
        String s = xt(str1, str2);
        tv.setText(s);
        if (s.equals("偏低")) {
            tv.setBackgroundResource(R.drawable.shape_round_low);
        }
        if (s.equals("正常")) {
            tv.setBackgroundResource(R.drawable.shape_round_green);
        }
        if (s.equals("偏高")) {
            tv.setBackgroundResource(R.drawable.shape_round_red);
        }
    }


}
