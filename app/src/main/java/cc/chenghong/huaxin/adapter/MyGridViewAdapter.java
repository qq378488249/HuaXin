package cc.chenghong.huaxin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.activity.R;

/**
 * 图片选择gv适配器
 */

public class MyGridViewAdapter extends CommonAdapter<String> {
    /**
     * 底部的确认按钮
     */
    private TextView tv;
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static ArrayList<String> mSelectedImage = new ArrayList<String>();
    /**
     * 文件夹路径
     */
    private String mDirPath;

    public MyGridViewAdapter(Context context, List<String> mDatas, int itemLayoutId,
                             String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
    }

    public MyGridViewAdapter(Context context, List<String> mDatas, ArrayList<String> listSelectImage, int itemLayoutId,
                             String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.mSelectedImage = listSelectImage;//选中的图片集合
    }

    @Override
    public void convert(ViewHolder helper, final String item, int position) {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
        //设置no_selected
        helper.setImageResource(R.id.id_item_select,
                R.drawable.picture_unselected);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mImageView.getLayoutParams();
        if (lp != null) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            lp.height = wm.getDefaultDisplay().getWidth() / 3;
            mImageView.setLayoutParams(lp);
        }
        mImageView.setLayoutParams(lp);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {
            //选择，则将图片变暗，反之则反之
            @Override
            public void onClick(View v) {

                // 已经选择过该图片
                if (mSelectedImage.contains(mDirPath + "/" + item)) {
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.picture_unselected);
                    mImageView.setColorFilter(null);
                } else
                // 未选择该图片
                {
                    if (mSelectedImage.size() > 6) {
                        Toast.makeText(mContext, "最多只能选择6张图片", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mSelectedImage.add(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.pictures_selected);
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                }
                if (tv != null) {
                    if (mSelectedImage.size() == 1) {
                        tv.setText("确认");
                    } else {
                        tv.setText("确认(" + (mSelectedImage.size() - 1) + ")");
                    }
                }
            }
        });

        /**
         * 已经选择过的图片，显示出选择过滤的效果(变暗)
         */
        if (mSelectedImage != null) {
            if (mSelectedImage.contains(mDirPath + "/" + item)) {
                mSelect.setImageResource(R.drawable.pictures_selected);
                mImageView.setColorFilter(Color.parseColor("#77000000"));
            }
        }
    }

    /**
     * 返回用户选择的图片集合
     *
     * @return
     */
    public ArrayList<String> getSelectListImages() {
        return mSelectedImage;
    }

    public void setTv(TextView textView) {
        this.tv = textView;
    }
}
