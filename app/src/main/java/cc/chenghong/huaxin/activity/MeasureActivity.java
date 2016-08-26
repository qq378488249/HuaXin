package cc.chenghong.huaxin.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cc.chenghong.huaxin.App;

/**
 * 健康测量 hcl 20160224
 */
public class MeasureActivity extends BaseActivity {
    /**
     * 蓝牙打开成功返回码
     */
    public final static int OK = 120;
    @ViewInject(R.id.ll_1)
    LinearLayout ll_1;
    @ViewInject(R.id.ll_2)
    LinearLayout ll_2;
    @ViewInject(R.id.ll_3)
    LinearLayout ll_3;

    //判断蓝牙是否打开
    private boolean isBlue = false;
    //蓝牙适配器
    BluetoothAdapter blueadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.content_measure);
        initXutils();
        setTitleName("健康测量");
        blueadapter = BluetoothAdapter.getDefaultAdapter();
    }

    @OnClick({R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4,R.id.bt})
    public void onClick(View v) {
        if(blueadapter == null){
            App.toask("您的设备不支持蓝牙");
            return;
        }
        switch (v.getId()) {
            case R.id.ll_1:
//                App.toask("1");
                if(blueadapter.isEnabled()){
                    startActivity(new Intent(MeasureActivity.this, PressureTestActivity.class));
                }else{
                    open_blue(1);
                }
                break;
            case R.id.ll_2:
                if(blueadapter.isEnabled()){
                    startActivity(new Intent(MeasureActivity.this, PressureTestActivity.class).putExtra("code", 2));
                }else{
                    open_blue(2);
                }
                break;
            case R.id.ll_3:
                if(blueadapter.isEnabled()){
                    startActivity(new Intent(MeasureActivity.this, PressureTestActivity.class).putExtra("code", 3));
                }else{
                    open_blue(3);
                }
                break;
            case R.id.bt:
                startActivity(new Intent(MeasureActivity.this, TraceActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == OK){
            switch(requestCode){
                case 1:
                    startActivity(new Intent(MeasureActivity.this, PressureTestActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MeasureActivity.this, PressureTestActivity.class).putExtra("code",2));
                    break;
                case 3:
                    startActivity(new Intent(MeasureActivity.this, PressureTestActivity.class).putExtra("code",3));
                    break;
            }
        }
    }
    //1血压，2血糖，3体脂
    private  void open_blue(int i){
        // 请求打开 Bluetooth
        Intent requestBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
        requestBluetoothOn.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // 设置 Bluetooth 设备可见时间
        requestBluetoothOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
        // 请求开启 Bluetooth
        startActivityForResult(requestBluetoothOn, i);
    }
}
