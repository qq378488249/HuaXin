package cc.chenghong.huaxin.camera_code;

import java.nio.IntBuffer;
import java.sql.SQLOutput;
import java.text.DecimalFormat;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SyncStatusObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.opengl.GLException;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import cc.chenghong.huaxin.utils.AnimationUtils;


public class UITools {
	static  DecimalFormat fmt   =new DecimalFormat("##,###,###,###,##");

	public static Application appInstance;

	public static void init(Application app){
		appInstance = app;
	}

	/**
	 * sp转换为px
	 * @param spValue
	 * @return
	 */
	public static int sp2px(float spValue) {
		float fontScale = appInstance.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static  void formatEdittext(EditText ed){
		ed.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				s = fmt.format(s);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}
	/**
	 * dp转换为px
	 * @param spValue
	 * @return
	 */
	public static int dp2px(float spValue) {
		float fontScale = appInstance.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/** 
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 */  
	public static int dip2px(float dpValue) {
		System.out.println(appInstance.toString());
		float a = appInstance.getResources().getDisplayMetrics().density;
		final float scale = appInstance.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	}  

	/** 
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */  
	public static int px2dip(float pxValue) {  
		final float scale = appInstance.getResources().getDisplayMetrics().density;  
		return (int) (pxValue / scale + 0.5f);  
	}

	/**
	 * 获取View在屏幕上的位置
	 * @param view
	 * @return
	 */
	public static Point getPos(View view){
		int[] pos = new int[2];
		view.getLocationOnScreen(pos);
		return new Point(pos[0], pos[1]);
	}

	public static PointF getPosF(View view){
		int[] pos = new int[2];
		view.getLocationOnScreen(pos);
		return new PointF(pos[0], pos[1]);
	}

	/**
	 * 获取View在屏幕上的位置
	 * @param view
	 * @return
	 */
	public static boolean viewContains(View view, PointF point){
		int[] pos = new int[2];
		view.getLocationOnScreen(pos);
		RectF r = new RectF(pos[0], pos[1], pos[0]+view.getMeasuredWidth(), pos[1]+view.getMeasuredHeight());
		return r.contains(point.x, point.y);
	}

	/**
	 * 获取View在屏幕上的位置
	 * @param view
	 * @return
	 */
	public static boolean viewContains(View view, float x, float y){
		return viewContains(view, new PointF(x, y));
	}

	/**
	 * 获取截图
	 * @param view
	 * @return
	 */
	public static Bitmap getScreenshot(View view){
		Bitmap bitmap = null;
		//截屏之前隐藏截图框
		view.invalidate();
		view.setDrawingCacheEnabled(true);
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		view.destroyDrawingCache();
		view.buildDrawingCache();
		bitmap = view.getDrawingCache();
		view.setDrawingCacheEnabled(false);
		return bitmap;
	}
	
	public static Bitmap getScreenshot2(View view){
		Bitmap bitmap = null;
		//截屏之前隐藏截图框
		view.invalidate();
		view.setDrawingCacheEnabled(true);
		view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		view.destroyDrawingCache();
		view.buildDrawingCache();
		Bitmap src = view.getDrawingCache();
		bitmap = Bitmap.createBitmap(src);
		view.setDrawingCacheEnabled(false);
		return bitmap;
	}
	
	public Bitmap getScreenshotFromGLSurface(int x, int y, int w, int h, GL10 gl)
	        throws OutOfMemoryError {
	    int bitmapBuffer[] = new int[w * h];
	    int bitmapSource[] = new int[w * h];
	    IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
	    intBuffer.position(0);

	    try {
	        gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
	        int offset1, offset2;
	        for (int i = 0; i < h; i++) {
	            offset1 = i * w;
	            offset2 = (h - i - 1) * w;
	            for (int j = 0; j < w; j++) {
	                int texturePixel = bitmapBuffer[offset1 + j];
	                int blue = (texturePixel >> 16) & 0xff;
	                int red = (texturePixel << 16) & 0x00ff0000;
	                int pixel = (texturePixel & 0xff00ff00) | red | blue;
	                bitmapSource[offset2 + j] = pixel;
	            }
	        }
	    } catch (GLException e) {
	        return null;
	    }

	    return Bitmap.createBitmap(bitmapSource, w, h, Config.ARGB_8888);
	}

	/**
	 * 获取Drawable第一个像素的颜色
	 * @param drawable
	 * @return
	 */
	public static int getDrawableColor(Drawable drawable){
		// ColorDrawable colorDrawable=((ColorDrawable) activityLayout.getBackground());
		Bitmap bitmap= Bitmap.createBitmap(1, 1, Config.ARGB_4444);
		Canvas canvas= new Canvas(bitmap);
		drawable.draw(canvas);
		int pix = bitmap.getPixel(0, 0);
		return pix;
	}

	/**
	 * 利用父元素的Padding给ScrollView添加弹性
	 * @param scrollView
	 * @param padding
	 */
	public static void elasticPadding(final ScrollView scrollView, final int padding){
		View child = scrollView.getChildAt(0);
		//记录以前的padding
		final int oldpt = child.getPaddingTop();
		final int oldpb = child.getPaddingBottom();
		//设置新的padding
		child.setPadding(child.getPaddingLeft(), padding+oldpt, child.getPaddingRight(), padding+oldpb);

		//添加视图布局完成事件监听
		scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			private boolean inTouch = false; //手指是否按下状态

			@SuppressLint("NewApi")
			private void disableOverScroll(){
				scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
			}

			/**  滚动到顶部 */
			private void scrollToTop(){
				scrollView.smoothScrollTo(scrollView.getScrollX(), padding-oldpt);
			}

			/** 滚动到底部 */
			private void scrollToBottom(){
				scrollView.smoothScrollTo(scrollView.getScrollX(), scrollView.getChildAt(0).getBottom()-scrollView.getMeasuredHeight()-padding+oldpb);
			}

			/** 检测scrollView结束以后,复原位置 */
			private final Runnable checkStopped = new Runnable() {
				@Override
				public void run() {
					int y = scrollView.getScrollY();
					int bottom = scrollView.getChildAt(0).getBottom()-y-scrollView.getMeasuredHeight();
					if(y <= padding && !inTouch){
						scrollToTop();
					}else if(bottom<=padding && !inTouch){
						scrollToBottom();
					}
				}
			};

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				//移除监听器
				scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				//设置最小高度
				//scrollView.getChildAt(0).setMinimumHeight(scrollView.getMeasuredHeight());
				//取消overScroll效果
				if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD){
					disableOverScroll();
				}

				scrollView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN){
							inTouch = true;
						}else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
							inTouch = false;
							//手指弹起以后检测一次是否需要复原位置
							scrollView.post(checkStopped);
						}
						return false;
					}
				});

				scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						if(!inTouch && scrollView!=null && scrollView.getHandler()!=null){//如果持续滚动,移除checkStopped,停止滚动以后只执行一次检测任务
							scrollView.getHandler().removeCallbacks(checkStopped);
							scrollView.postDelayed(checkStopped, 100);
						}
					}
				});

				//第一次加载视图,复原位置
				scrollView.postDelayed(checkStopped, 300);
			}
		});
	}

	/**
	 * 利用父元素的Padding给HorizontalScrollView添加弹性
	 * @param scrollView
	 * @param padding
	 */
	public static void elasticPadding(final HorizontalScrollView scrollView, final int padding){
		Log.i("", "elasticPadding>>>>!!");
		View child = scrollView.getChildAt(0);

		//记录以前的padding
		final int oldpt = child.getPaddingTop();
		final int oldpb = child.getPaddingBottom();
		//设置新的padding
		child.setPadding(padding+oldpt, child.getPaddingTop(), padding+oldpb, child.getPaddingBottom());

		//添加视图布局完成事件监听
		scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			private boolean inTouch = false; //手指是否按下状态

			@SuppressLint("NewApi")
			private void disableOverScroll(){
				scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
			}

			/**  滚动到左边 */
			private void scrollToLeft(){
				scrollView.smoothScrollTo(padding-oldpt, scrollView.getScrollY());
			}

			/** 滚动到底部 */
			private void scrollToRight(){
				scrollView.smoothScrollTo(scrollView.getChildAt(0).getRight()-scrollView.getMeasuredWidth()-padding+oldpb, scrollView.getScrollY());
			}

			/** 检测scrollView结束以后,复原位置 */
			private final Runnable checkStopped = new Runnable() {
				@Override
				public void run() {
					int x = scrollView.getScrollX();
					int bottom = scrollView.getChildAt(0).getRight()-x-scrollView.getMeasuredWidth();
					if(x <= padding && !inTouch){
						scrollToLeft();
					}else if(bottom<=padding && !inTouch){
						scrollToRight();
					}
				}
			};

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				//移除监听器
				scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				//设置最小宽度
				scrollView.getChildAt(0).setMinimumWidth(scrollView.getMeasuredWidth());
				//取消overScroll效果
				if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD){
					disableOverScroll();
				}

				scrollView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN){
							inTouch = true;
						}else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
							inTouch = false;
							//手指弹起以后检测一次是否需要复原位置
							scrollView.post(checkStopped);
						}
						return false;
					}
				});

				scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						//如果持续滚动,移除checkStopped,停止滚动以后只执行一次检测任务
						if(!inTouch && scrollView!=null && scrollView.getHandler()!=null){
							scrollView.getHandler().removeCallbacks(checkStopped);
							scrollView.postDelayed(checkStopped, 100);
						}
					}
				});

				//第一次加载视图,复原位置
				scrollView.postDelayed(checkStopped, 300);
			}
		});
	}

	public static void fadeIn(View view){
		fadeIn(view, 300);
	}

	public static void fadeIn(final View view, int duration){
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}
		});
		aa.setFillAfter(true);
		aa.setDuration(duration);
		view.startAnimation(aa);
	}

	public static void fadeOut(View view){
		fadeOut(view, 300);
	}

	public static void fadeOut(final View view, int duration){
		AlphaAnimation aa = new AlphaAnimation(1, 0);
		aa.setFillAfter(true);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.INVISIBLE);
			}
		});
		aa.setDuration(duration);
		view.startAnimation(aa);
	}

	/**
	 * 给ListView添加弹性
	 * @param list
	 * @param padding
	 */
	public static void elasticListView(final ListView list, int padding){
		final TextView header = new TextView(list.getContext());
		final TextView footer = new TextView(list.getContext());
		header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, padding));
		footer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, padding));
		ListAdapter adapter = list.getAdapter();
		list.setAdapter(null);
		list.addHeaderView(header);
		list.addFooterView(footer);
		list.setAdapter(adapter);

		list.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			private boolean inTouch = false; //手指是否按下状态

			/** 检测ListView结束以后,复原位置 */
			private final Runnable checkStopped = new Runnable() {
				@Override
				public void run() {
					View child = list.getChildAt(0);
					if(child == header){
						int by = child.getMeasuredHeight()+child.getTop();
						list.smoothScrollBy(by, 500);
					}else{
						child = list.getChildAt(list.getChildCount()-1);
						if(child == footer){
							int by = child.getTop() - list.getMeasuredHeight() ;
							list.smoothScrollBy(by, 500);
						}
					}
				}
			};

			@Override
			public void onGlobalLayout() {
				list.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				list.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN){
							inTouch = true;
						}else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
							inTouch = false;
							//手指弹起以后检测一次是否需要复原位置
							list.postDelayed(checkStopped, 100);
						}
						return false;
					}
				});

				list.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {

					@Override
					public void onScrollChanged() {
						list.getHandler().removeCallbacks(checkStopped);
						if(!inTouch){
							list.postDelayed(checkStopped, 100);
						}
					}
				});
			}
		});
	}

	public interface OnDragListener{
		public enum DragEvent{
			ACTION_START,
			ACTION_DRAG,
			ACTION_END
		}
		public void onDrag(View view, MotionEvent event, DragEvent action, int touchSlop);
	}

	/**
	 * 设置拖动事件监听器
	 * @param view
	 * @param onDragListener
	 */
	public static DragOnTouchListenerHolder setOnDragListener(final View view, final OnDragListener onDragListener,
			final boolean dispatchTouchEvent, final boolean enable){
		final DragOnTouchListenerHolder dragOnTouchListenerHolder = new DragOnTouchListenerHolder();
		view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				DragOnTouchListener d = new DragOnTouchListener(view, onDragListener, dispatchTouchEvent);
				dragOnTouchListenerHolder.dragOnTouchListener = d;
				d.setEnable(enable);
				view.setOnTouchListener(d);
				if(!(view instanceof ListView)) view.setOnClickListener(null);
			}
		});
		return dragOnTouchListenerHolder;
	}

	/**
	 * 切换按钮的selected状态
	 * @param btn1
	 * @param btn2
	 * @param btn3
	 * @param btn4
	 * @param current 当前按钮
	 */
	public static void switchButton(View btn1, View btn2, View btn3, View btn4, View current){
		View[] buttons = new View[]{btn1, btn2, btn3, btn4};
		for(View v : buttons){
			v.setSelected(v == current? true: false);
		}
	}
	/**
	 * 切换按钮的selected状态
	 * @param buttons
	 * @param current 当前按钮
	 */
	public static void switchButton(View[] buttons, View current){
		for(View v : buttons){
			v.setSelected(v == current? true: false);
		}
	}
	/**
	 * 切换按钮的selected状态
	 * @param buttons
	 * @param current 当前按钮
	 */
	public static void switchButton(View[] buttons, int current){
		for(int i=0; i<buttons.length; i++){
			buttons[i].setSelected(i == current? true: false);
		}
	}
	/**
	 * 切换按钮的显示状态
	 * @param buttons
	 * @param current
	 */
	public static void switchShow(View[] buttons, int current){
		for(int i=0; i<buttons.length; i++){
			buttons[i].setVisibility(current == i? View.VISIBLE: View.GONE);
		}
	}

	public static Drawable convertToGrayscale(Drawable drawable){
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
		drawable.setColorFilter(filter);
		return drawable;
	}

	public static boolean isEmpty(EditText text){
		return text.getText().toString().length() == 0;
	}

	public static boolean isEmptyTrim(EditText text){
		return text.getText().toString().trim().length() == 0;
	}

	/**
	 * 从当前Dialog中查找DatePicker子控件
	 * 
	 * @param group
	 * @return
	 */
	public static DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}
	
	public static void delayFadeout(final View view, int millisec){
		view.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(view.getVisibility() == View.VISIBLE){
					AnimationUtils.fadeOut(view, 300, null);
				}
			}
		}, millisec);
	}
}
