package cc.chenghong.huaxin.camera_code;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class DragOnTouchListener implements OnTouchListener{
		private UITools.OnDragListener onDragListener;
		private View view;
		private boolean dispatchTouchEvent;
		public DragOnTouchListener(View view, UITools.OnDragListener onDragListener,
				boolean dispatchTouchEvent) {
			this.onDragListener = onDragListener;
			this.view = view;
			this.dispatchTouchEvent = dispatchTouchEvent;
		}
		private int mTouchSlop = 5;
		private boolean mScolling = false;//手指拖拽状态
		private PointF mLastPoint = null;
		private int currentSlop = 0;
		private boolean enable = false;
		
		public void setDispatchTouchEvent(boolean dispatchTouchEvent) {
			this.dispatchTouchEvent = dispatchTouchEvent;
		}
		
		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		/**
		 * 计算两个坐标x/y的宽高比 如果x/y>1即x大于y，说明是横向滑动，不应该允许拖动
		 * @param p1
		 * @param p2
		 * @return
		 */
		private float calculateGradient(PointF p1, PointF p2){
			float dx = p1.x - p2.x;
			float dy = p1.y - p2.y;
			return Math.abs(dx/dy);
		}

		@Override
		public boolean onTouch(View v, final MotionEvent ev) {
			if(!enable) return false;
			float eventFloatY = ev.getY();
			if(ev.getAction() == MotionEvent.ACTION_DOWN ){//记录第一次触摸Y
				mLastPoint = new PointF(ev.getX(), ev.getY());
			}
			if(ev.getAction() == MotionEvent.ACTION_MOVE){
				if(mLastPoint == null){
					mLastPoint = new PointF(ev.getX(), ev.getY());
				}
				if(!mScolling){
					//计算移动的Y和第一次触摸Y，大于touchSlop开始移动 enablePullDown=true且slop>0时允许下拉 enablePullUp=true且slop<0时允许上拉
					float slop =eventFloatY - mLastPoint.y;
					currentSlop = (int)slop;
					if(Math.abs(slop)>=mTouchSlop){
						//判断斜度,如果接近横向滑动，不允许拉动
						PointF currentP = new PointF(ev.getX(), ev.getY());
						float grad = calculateGradient(mLastPoint, currentP);
						if(grad<1){//开始scroll
							mScolling = true;
							onDragListener.onDrag(view, ev, UITools.OnDragListener.DragEvent.ACTION_START, currentSlop);
						}
					}
				}else{
					view.post(new Runnable() {
						@Override
						public void run() {
							onDragListener.onDrag(view, ev, UITools.OnDragListener.DragEvent.ACTION_DRAG, currentSlop);
						}
					});
					//return true;
				}
			}else if(ev.getAction() == MotionEvent.ACTION_CANCEL
					|| ev.getAction() == MotionEvent.ACTION_UP){
				//手指离开屏幕
				if(mScolling){
					mScolling = false;
					onDragListener.onDrag(view, ev, UITools.OnDragListener.DragEvent.ACTION_END, currentSlop);
					return true;
				}
				mLastPoint = null;
			}
			if(dispatchTouchEvent){
				return false;
			}else{
				return true;
			}
		}
	}