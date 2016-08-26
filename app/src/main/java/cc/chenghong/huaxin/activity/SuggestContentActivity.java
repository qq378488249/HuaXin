package cc.chenghong.huaxin.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;

/**
 * 健康预警、营养方案、运动建议、服药提醒
 */
public class SuggestContentActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;

    List<Object> list = new ArrayList<Object>();
    CommonAdapter<Object> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_suggest_content);
        initXutils();
        init();
    }

    private void init() {
        switch (code ){
            case 0:
                adapter = new CommonAdapter<Object>(this,list,R.layout.lv_item_suggest) {
                    @Override
                    public void convert(ViewHolder helper, Object item, int position) {

                    }
                };
                break;
        }
    }

}
