package cc.chenghong.huaxin.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class AnimationUtils {
    
    public interface AnimationListener{
        
    }
    
    public interface AnimationEndListener extends AnimationListener{
        public void onAnimationEnd(View view);
    }
    

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void doFadeIn(final View view, int duration, final AnimationEndListener listener){
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f).setDuration(duration);
            oa.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }
                
                @Override
                public void onAnimationRepeat(Animator animation) {
                    
                }
                
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(listener != null)
                    listener.onAnimationEnd(view);
                }
                
                @Override
                public void onAnimationCancel(Animator animation) {
                    
                }
            });
        oa.start();
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void doFadeOut(final View view, int duration, final AnimationEndListener listener){
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f).setDuration(duration);
        if(listener != null){
            oa.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    
                }
                
                @Override
                public void onAnimationRepeat(Animator animation) {
                    
                }
                
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    listener.onAnimationEnd(view);
                }
                
                @Override
                public void onAnimationCancel(Animator animation) {
                    
                }
            });
        }
        oa.start();
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void slideInLeftSupport(final View[] views, final int[] durations){
		/**
		 * 首先动画移动View到屏幕左边，动画完成后将View设置为可见
		 */
		android.animation.AnimatorSet set = new android.animation.AnimatorSet();
		android.animation.AnimatorSet.Builder builder = null;
		for(int i=0; i<views.length; i++){
			final View v = views[i];
			ObjectAnimator o = ObjectAnimator.ofInt(v, "left", v.getLeft()-v.getMeasuredWidth()).setDuration(0);
			o.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {}
				@Override
				public void onAnimationRepeat(Animator animation) {}
				@Override
				public void onAnimationEnd(Animator animation) {
					v.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationCancel(Animator animation) {}
			});
			if(i == 0){
				builder = set.play(o);
			}else{
				builder = builder.with(o);
			}
		}
		set.start();
		/**
		 * 隐藏动画完成后，开始移出动画
		 */
		set.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {}
			@Override
			public void onAnimationRepeat(Animator animation) {}
			@Override
			public void onAnimationEnd(Animator animation) {
				android.animation.AnimatorSet set = new android.animation.AnimatorSet();
				android.animation.AnimatorSet.Builder builder = null;
				/**
				 * 将View逐个动画移出来
				 */
				for(int i=0; i<views.length; i++){
					View v = views[i];
					ObjectAnimator o = ObjectAnimator.ofInt(v, "left",
							v.getLeft(), v.getLeft()+v.getMeasuredWidth())
							.setDuration(durations[i]);
					o.setInterpolator(new DecelerateInterpolator());
					if(i == 0){
						builder = set.play(o);
					}else{
						builder = builder.with(o);
					}
				}
				set.start();
			}
			@Override
			public void onAnimationCancel(Animator animation) {}
		});
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void slideOutLeftSupport(final View[] views, final int[] durations){
        /**
         * 首先动画移动View到屏幕左边，动画完成后将View设置为可见
         */
        android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        android.animation.AnimatorSet.Builder builder = null;
        for(int i=0; i<views.length; i++){
            final View v = views[i];
            ObjectAnimator o = ObjectAnimator.ofInt(v, "left",
                    v.getLeft(), v.getLeft()-v.getMeasuredWidth())
                    .setDuration(durations[i]);
            o.setInterpolator(new DecelerateInterpolator());
            /*o.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setVisibility(View.VISIBLE);
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
            });*/
            if(i == 0){
                builder = set.play(o);
            }else{
                builder = builder.with(o);
            }
        }
        set.start();
    }
	
	/**
	 * 从左边滑动出来
	 * 1.如果要用此动画，请将View的属性默认设置为INVISIBLE
	 * 2.低于Android3.0的版本不执行动画，直接将View设置为Visible
	 * @param views 要执行动画的View数组
	 * @param durations 对应的动画时间
	 */
	public static void slideInLeft(View[] views, int[] durations){
		if(supportAnimation()){
			slideInLeftSupport(views, durations);
		}else{
			for(View v : views){
				v.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * 动画左边移出
	 * @param views
	 * @param durations
	 */
	public static void slideOutLeft(View[] views, int[] durations){
	    if(supportAnimation()){
            slideOutLeftSupport(views, durations);
        }else{
            for(View v : views){
                v.setVisibility(View.INVISIBLE);
            }
        }
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void slideDwonSupport(final View[] views, final int[] durations){
		/**
		 * 首先动画移动View到屏幕左边，动画完成后将View设置为可见
		 */
		android.animation.AnimatorSet set = new android.animation.AnimatorSet();
		android.animation.AnimatorSet.Builder builder = null;
		for(int i=0; i<views.length; i++){
			final View v = views[i];
			ObjectAnimator o = ObjectAnimator.ofInt(v, "top", v.getTop()-v.getMeasuredHeight()).setDuration(0);
			o.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {}
				@Override
				public void onAnimationRepeat(Animator animation) {}
				@Override
				public void onAnimationEnd(Animator animation) {
					v.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationCancel(Animator animation) {}
			});
			if(i == 0){
				builder = set.play(o);
			}else{
				builder = builder.with(o);
			}
		}
		set.start();
		/**
		 * 隐藏动画完成后，开始移出动画
		 */
		set.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {}
			@Override
			public void onAnimationRepeat(Animator animation) {}
			@Override
			public void onAnimationEnd(Animator animation) {
				android.animation.AnimatorSet set = new android.animation.AnimatorSet();
				android.animation.AnimatorSet.Builder builder = null;
				/**
				 * 将View逐个动画移出来
				 */
				for(int i=0; i<views.length; i++){
					View v = views[i];
					ObjectAnimator o = ObjectAnimator.ofInt(v, "top",
							v.getTop(), v.getTop()+v.getMeasuredHeight())
							.setDuration(durations[i]);
					o.setInterpolator(new DecelerateInterpolator());
					if(i == 0){
						builder = set.play(o);
					}else{
						builder = builder.with(o);
					}
				}
				set.start();
			}
			@Override
			public void onAnimationCancel(Animator animation) {}
		});
	}
	
	/**
	 * 从上边滑动下来
	 * 1.如果要用此动画，请将View的属性默认设置为INVISIBLE
	 * 2.低于Android3.0的版本不执行动画，直接将View设置为Visible
	 * @param views 要执行动画的View数组
	 * @param durations 对应的动画时间
	 */
	public static void slideDwon(View[] views, int[] durations){
		if(supportAnimation()){
			slideDwonSupport(views, durations);
		}else{
			for(View v : views){
				v.setVisibility(View.VISIBLE);
			}
		}
	}
    
    public static boolean supportAnimation(){
        return VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
    
    public static void fadeIn(View view, int duration,  AnimationEndListener listener){
        if(supportAnimation()){
            doFadeIn(view, duration, listener);
        }else{
            view.setVisibility(View.VISIBLE);
            listener.onAnimationEnd(view);
        }
    }
    
    public static void fadeOut(View view, int duration,  AnimationEndListener listener){
        if(supportAnimation()){
            doFadeOut(view, duration, listener);
        }else{
            view.setVisibility(View.GONE);
            listener.onAnimationEnd(view);
        }
    }
}
