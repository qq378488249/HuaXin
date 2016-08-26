package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 体脂测量
 */
public class FatTestActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_fat_test);
        initXutils();
        setTitleName("体脂测量");
        iv_rigth.setVisibility(View.VISIBLE);
        iv_rigth.setBackgroundResource(R.drawable.icon_add);
    }

    @OnClick({R.id.iv_rigth,R.id.tv_test})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_rigth:
                startActivity(new Intent(this,RecordPressureActivity.class).putExtra("code",3));
                break;
            case R.id.tv_test:

                break;
        }
    }
}
