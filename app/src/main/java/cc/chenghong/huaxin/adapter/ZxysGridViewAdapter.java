package cc.chenghong.huaxin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import cc.chenghong.huaxin.activity.ConsultActivity;
import cc.chenghong.huaxin.activity.R;
import cc.chenghong.huaxin.camera_code.UITools;
import cc.chenghong.huaxin.loader.ImageLoader;

/**
 * 咨询医生gv适配器
 * Created by hcl on 2016/5/4.
 */
public class ZxysGridViewAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;

    public ZxysGridViewAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder = null;
//        if (null == convertView) {
//            viewHolder = new ViewHolder();
//
//            convertView.setTag(viewHolder);
//            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
        convertView = LayoutInflater.from(context).inflate(R.layout.gv_item_photo, null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp.width = (wm.getDefaultDisplay().getWidth() - UITools.dip2px(20)) / 4;
        lp.height = lp.width;
        iv.setLayoutParams(lp);
        iv.setImageResource(R.drawable.pictures_no);
//        System.out.println(list.size());
        if (list.get(position).equals(ConsultActivity.IV_ADD)) {
            iv.setImageResource(R.drawable.jh);
        } else {
            if (list.get(position).indexOf("http:") != -1) {//说明是网络图片
                //显示图片的配置
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.c6)
                        .showImageOnFail(R.drawable.c6)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(list.get(position), iv, options);
            } else {
                ImageLoader.getInstance().loadImage(list.get(position), iv);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
