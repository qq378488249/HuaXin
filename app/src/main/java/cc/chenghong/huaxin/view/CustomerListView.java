package cc.chenghong.huaxin.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

/**
 * Created by hcl on 2016/6/17.
 */
public class CustomerListView extends ListView {


    private Context mContext;
    private boolean outBound = false;
    private int distance;
    private int firstOut;

    public CustomerListView(Context c) {
        super(c);
        this.mContext = c;
    }

    public CustomerListView(Context c,AttributeSet attrs) {
        super(c,attrs);
        this.mContext = c;
    }

    public CustomerListView(Context c,AttributeSet attrs,int defStyle) {
        super(c,attrs,defStyle);
        this.mContext = c;
    }

    GestureDetector gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            int firstPos = getFirstVisiblePosition();
            int lastPos = getLastVisiblePosition();
            int itemCount = getCount();
            if(outBound && firstPos != 0 && lastPos != (itemCount - 1)){
                scrollTo(0, 0);
                return false;
            }
            View firstView = getChildAt(firstPos);
            if(!outBound){
                firstOut = (int) e2.getRawX();
            }
            if(firstView != null && (outBound || (firstPos == 0 && firstView.getTop() == 0 && distanceY < 0))){
                distance = (int) (firstOut - e2.getRawY());
                scrollBy(0, distance/2);
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {


            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {


            return false;
        }
    });

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int act = ev.getAction();
        if((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL) && outBound){
            outBound = false;
        }
        if(!gestureDetector.onTouchEvent(ev)){
            outBound = false;
        }else{
            outBound = true;
        }
        Rect rect = new Rect();
        getLocalVisibleRect(rect);
        TranslateAnimation am = new TranslateAnimation(0,0,-rect.top,0);
        am.setDuration(300);
        startAnimation(am);
        scrollTo(0,0);
        return super.dispatchTouchEvent(ev);
    };
}
