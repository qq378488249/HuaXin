package cc.chenghong.huaxin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 浏览用药
 */
public class BrowseDrugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_browse_drug);
        initXutils();
        setTitleName("浏览用药");
        iv_rigth.setVisibility(View.VISIBLE);
        iv_rigth.setBackgroundResource(R.drawable.icon_add);
    }

    @OnClick({R.id.iv_rigth})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_rigth:
                //实例化一个intent，并指定action
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //指定一个图片路径对应的file对象
//                uri = Uri.fromFile(ImageUtil.getImageFile());
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //启动activity
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 1 && resultCode==RESULT_OK){
            if(data != null){ //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //返回有缩略图
                if(data.hasExtra("data")){
                    Bitmap bitmap = data.getParcelableExtra("data");
                    Intent intent = new Intent(this,InputDrugActivity.class).putExtra("data", bitmap);
                    intent.putExtra("code" ,2);//输入用药信息
                    startActivity(intent);
                    //得到bitmap后的操作
                }
            }else{
                //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 通过目标uri，找到图片
                // 对图片的缩放处理
                // 操作
            }
        }
    }
}
