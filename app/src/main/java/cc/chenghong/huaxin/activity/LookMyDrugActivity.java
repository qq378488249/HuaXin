package cc.chenghong.huaxin.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * 查看我的用药信息 thin blue 20160226
 */
public class LookMyDrugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_look_my_drug);
        initXutils();
        setTitleName("查看我的用药信息");
    }

}
