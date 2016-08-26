package cc.chenghong.huaxin.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.huaxin.adapter.MyGridViewAdapter;
import cc.chenghong.huaxin.entity.ImageFloder;
import cc.chenghong.huaxin.popup_window.ListImageDirPopupWindow;

public class PhotoSelectActivity extends BaseActivity {
    @Bind(R.id.tv_dir)
    TextView tvDir;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.gv)
    GridView gv;
    @Bind(R.id.ll_bottom)
    RelativeLayout llBottom;
    @Bind(R.id.ll_bar)
    LinearLayout llBar;

    private ProgressDialog mProgressDialog;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
    /**
     * 文件数量
     */
    int totalCount = 0;

    MyGridViewAdapter mAdapter;
    private ListImageDirPopupWindow mListImageDirPopupWindow;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        ButterKnife.bind(this);
        statusBar(llBar);
        list = getIntent().getStringArrayListExtra("data");
        if (list != null) {
            if (list.size() > 1) {
                tvSubmit.setText("确认(" + (list.size() - 1) + ")");
            }
        }
        getImages();
    }

    @OnClick({R.id.tv_submit, R.id.tv_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                Intent intent = newIntent(ConsultActivity.class);
                intent.putExtra("data", mAdapter.getSelectListImages());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.tv_select:
                mListImageDirPopupWindow
                        .setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(llBottom, 0, 0);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.3f;
                getWindow().setAttributes(lp);
                break;
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            toask("没有检测到存储卡");
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "加载中...");
        //开启一个线程去扫描手机中的图片
        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PhotoSelectActivity.this.getContentResolver();

                // 只查询jpeg、jpg、png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png", "image/jpg"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                handler.sendEmptyMessage(1);

            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//             为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mImgs = Arrays.asList(mImgDir.list());
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new MyGridViewAdapter(getApplicationContext(), mImgs, list,
                R.layout.gv_item_photo_select, mImgDir.getAbsolutePath());
        gv.setAdapter(mAdapter);
        mAdapter.setTv(tvSubmit);
        String str[] = mImgDir.getAbsolutePath().split("/");
        //获取到最多图片的文件夹
        ImageFloder imageFloder = getImageMaxImageFloder();
        imageFloder.setIsSelect(true);
        tvDir.setText(imageFloder.getName().substring(1) + "(" + imageFloder.getCount() + "张)");
        //获取第一个的
//        tvCount.setText(imageFloder.getCount() + "");
        mProgressDialog.dismiss();
    }

    /**
     * 获取图片数量最多的文件夹
     *
     * @return
     */
    private ImageFloder getImageMaxImageFloder() {
        ImageFloder imageFloder = new ImageFloder();
        imageFloder.setCount(0);
        for (int i = 0; i < mImageFloders.size(); i++) {
            if (mImageFloders.get(i).getCount() > imageFloder.getCount()) {
                imageFloder = mImageFloders.get(i);
            }
        }
        return imageFloder;
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (getWindowManager().getDefaultDisplay().getHeight() * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(new ListImageDirPopupWindow.OnImageDirSelected() {
            @Override
            public void selected(ImageFloder floder, int position) {
                for (int i = 0; i < mImageFloders.size(); i++) {
                    mImageFloders.get(i).setIsSelect(false);
                }
                mImageFloders.get(position).setIsSelect(true);
                floder = mImageFloders.get(position);
                mImgDir = new File(floder.getDir());
                mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                || filename.endsWith(".jpeg"))
                            return true;
                        return false;
                    }
                }));
                /**
                 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
                 */
                mAdapter = new MyGridViewAdapter(getApplicationContext(), mImgs,
                        R.layout.gv_item_photo_select, mImgDir.getAbsolutePath());
                mAdapter.setTv(tvSubmit);
                gv.setAdapter(mAdapter);
                tvDir.setText(floder.getName().substring(1) + "(" + floder.getCount() + "张)");
                mListImageDirPopupWindow.dismiss();
            }
        });
    }
}
