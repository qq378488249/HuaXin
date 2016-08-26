package cc.chenghong.huaxin.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * Created by hcl on 2016/6/17.
 */
public class ZhangPhilListView extends ListView {

    // 这个值控制可以把ListView拉出偏离顶部或底部的距离。
    private static final int MAX_OVERSCROLL_Y = 200;

    private Context mContext;
    private int newMaxOverScrollY;

    public ZhangPhilListView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ZhangPhilListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

	/*
	 * public ZhangPhilListView(Context context, AttributeSet attrs, int
	 * defStyle) { super(context, attrs, defStyle); this.mContext = context;
	 * init(); }
	 */

    private void init() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float density = metrics.density;
        newMaxOverScrollY = (int) (density * MAX_OVERSCROLL_Y);
    }

    // 最关键的地方。
    //支持到SDK8需要增加@SuppressLint("NewApi")。
    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, newMaxOverScrollY,
                isTouchEvent);
    }
}
