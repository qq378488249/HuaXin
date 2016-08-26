package cc.chenghong.huaxin.activity;

import android.os.Bundle;

/**
 * 血糖测量
 */
public class TestSugarActivity extends BaseActivity {

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_test_press);
        initXutils();
        setTitleName("血糖测量");
    }

}
