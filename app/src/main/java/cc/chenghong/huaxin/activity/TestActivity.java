package cc.chenghong.huaxin.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.entity.Suggest_item;
import cc.chenghong.huaxin.request.BaseRequest;
import cc.chenghong.huaxin.view.MyAdapter;
import cc.chenghong.huaxin.view.MyListener;
import cc.chenghong.huaxin.view.PullToRefreshLayout;
import cc.chenghong.huaxin.view.PullableListView;

public class TestActivity<T> extends BaseActivity {
    @ViewInject(R.id.et)
    EditText et;
    @ViewInject(R.id.iv_1)
    ImageView iv_1;
    @ViewInject(R.id.iv_2)
    ImageView iv_2;
//    @ViewInject(R.id.content_view)
//    PullableListView listView;
//    @ViewInject(R.id.refresh_view)
//    PullToRefreshLayout pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_test);
//        initXutils();
//        setTitleName("测试");
//        //设置下拉上拉监听器
//        BaseRequest.get(new String(), new Response.Listener() {
//            @Override
//            public void onResponse(Object o) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        pl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                // 下拉刷新操作
////                progress("刷新中...");
//                new Handler()
//                {
//                    @Override
//                    public void handleMessage(Message msg)
//                    {
//                        // 千万别忘了告诉控件刷新完毕了哦！
//                        pl.refreshFinish(PullToRefreshLayout.SUCCEED);
////                        hideProgress();
//                    }
//                }.sendEmptyMessageDelayed(0, 2000);
//            }
//
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
////                progress("加载中...");
//                new Handler()
//                {
//                    @Override
//                    public void handleMessage(Message msg)
//                    {
//                        // 千万别忘了告诉控件刷新完毕了哦！
//                        pl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
////                        hideProgress();
//                    }
//                }.sendEmptyMessageDelayed(0, 2000);
//            }
//        });
//        initListView();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1001 && resultCode == RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            Bitmap bm = (Bitmap) bundle.get("data");
//            iv_2.setImageBitmap(bm);
//        }else if (requestCode == 1002 && resultCode == RESULT_OK && data !=null ){
////            Uri selectedImage = data.getData();
////            String[] filePathColumn = { MediaStore.Images.Media.DATA };
////            Cursor cursor = getContentResolver().query(selectedImage,
////                    filePathColumn, null, null, null);
////            cursor.moveToFirst();
////            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////            String picturePath = cursor.getString(columnIndex);
////            cursor.close();
////            iv_2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//            Uri uri = data.getData();
//            Log.e("uri", uri.toString());
//            ContentResolver cr = this.getContentResolver();
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                iv_2.setImageBitmap(bitmap);
//                /* 将Bitmap设定到ImageView */
//            } catch (FileNotFoundException e) {
//                Log.e("Exception", e.getMessage(),e);
//            }
//        }
//    }
//
//    /**
//     * ListView初始化方法
//     */
//    private void initListView()
//    {
//        List<String> items = new ArrayList<String>();
//        for (int i = 0; i < 5; i++)
//        {
//            items.add("这里是item " + i);
//        }
//        MyAdapter adapter = new MyAdapter(this, items);
//        listView.setAdapter(adapter);
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
//        {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                                           int position, long id)
//            {
//                App.toask("长按了" + parent.getAdapter().getItemId(position));
//                return true;
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                App.toask("点击了" + parent.getAdapter().getItemId(position));
//            }
//        });
//    }
//
//    @OnClick({R.id.bt_1,R.id.bt_2})
//    public void onClick(View view) {
//        switch (view.getId()){
//            case  R.id.bt_1:
//                openInput(et);
//                break;
//            case  R.id.bt_2:
//                closeInput(et);
//                break;
//        }
//    }
    }
}
