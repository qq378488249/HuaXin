package cc.chenghong.huaxin.activity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ehlink.ehbledevice.scan.DeviceScan;
import com.ehlink.ehbledevice.scan.Xt;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.MyEvent;
import cc.chenghong.huaxin.entity.Xycl;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.utils.SharedPreferencesUtils;
import de.greenrobot.event.EventBus;

/**
 * 血压测量/血糖测量/体脂测量
 */
public class PressureTestActivity extends BaseActivity {
    public static String ServiceUUID = "11223344-5566-7788-99aa-bbccddeeff00";
    public static String SPP_CHAN_UUID = "00004a5b-0000-1000-8000-00805f9b34fb";
    private static final String EH_MC10_DEFAULT_UUID128 = "00FFEEDDCCBBAA998877665544332211";

    @ViewInject(R.id.bt_submit)
    Button bt_submit;
    @ViewInject(R.id.bt_up)
    Button bt_up;
    @ViewInject(R.id.bt_load)
    Button bt_load;
    @ViewInject(R.id.bt)
    Button bt;
    @ViewInject(R.id.ll)
    LinearLayout ll;
    @ViewInject(R.id.ll_top)
    LinearLayout ll_top;
    @ViewInject(R.id.ll_bar1)
    LinearLayout ll_bar1;
    @ViewInject(R.id.ll_1)
    LinearLayout ll_1;
    @ViewInject(R.id.ll_2)
    LinearLayout ll_2;
    @ViewInject(R.id.ll_3)
    LinearLayout ll_3;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.tv_1)
    TextView tv_1;
    @ViewInject(R.id.tv_2)
    TextView tv_2;
    @ViewInject(R.id.tv_3)
    TextView tv_3;
    @ViewInject(R.id.tv_4)
    TextView tv_4;
    @ViewInject(R.id.tv_5)
    TextView tv_5;
    @ViewInject(R.id.tv_num)
    TextView tv_num;
    @ViewInject(R.id.tv_kg)
    TextView tv_kg;

    String getUrl = "";
    String addUrl = "";
    Dialog dialog;
    TextView tv_ask;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private DeviceScan blesacn;
    boolean isOk = false;//是否搜索到了设备
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<BluetoothDevice>();
    CommonAdapter<BluetoothDevice> adapter;
    private static final int REQUEST_ENABLE_BT = 1;
    // 10秒后停止查找搜索.
    private static final long SCAN_PERIOD = 10000;
    //蓝牙设备通信
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattService sppService;
    private BluetoothGattCharacteristic sppCharacteristic;
    private BluetoothGattCharacteristic SwitchCharacteristic;
    /**
     * 是否连接成功
     */
    private boolean mConnected = false;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //    private ServiceConnection mServiceConnection;
    private String mDeviceName;
    private String mDeviceAddress;

    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//                toask("连接成功");
                bt_load.setText("已连接");
                mConnected = true;
                hideProgress();
//                Message message = Message.obtain();
//                message.what = 29;
//                mHandler.sendMessageDelayed(message, 3000);
//                updateConnectionState(R.string.connected);
                //playVoice();
//                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                toask("连接断开");
                bt_load.setText("未连接");
                mConnected = false;
//                updateConnectionState(R.string.disconnected);
//                invalidateOptionsMenu();
//                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
                AppGetSerivesAndCharacteristic();
                //Log.i("spp", "discover led service");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_test);
        EventBus.getDefault().register(this);
        initXutils();
        statusBar(ll_bar1);
        tv_center.setText("血压测量");
        set_ll(ll_1);
        getUrl = Api.xy_get_new;
        addUrl= Api.xt_add;
        if (code == 2) {
            tv_center.setText("血糖测量");
            set_ll(ll_2);
            ll_top.setBackgroundResource(R.drawable.pic08);
            getUrl = Api.xt_get_new;
            addUrl = Api.xt_add;
        }
        if (code == 3) {
            tv_center.setText("体脂测量");
            ll_top.setBackgroundResource(R.drawable.pic06);
            set_ll(ll_3);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_top.getLayoutParams();
            layoutParams.height = (int) (getHeight() * 0.618);
            ll_top.setLayoutParams(layoutParams);
            tv_title.setVisibility(View.GONE);
            getUrl = Api.tz_get_new;
            addUrl= Api.tz_add;
        }
        getData(true);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 29) {
                    SPP_SendHex(EhUtils.hexStringToBytes("26445A20063336380D"));
                }
                super.handleMessage(msg);
            }
        };
        if (!DeviceScan.isBLESupported(this)) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = DeviceScan.getManager(this);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void getData(final boolean b) {
        if (b) load();
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", App.getUser().getId());
        client().post(getUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    if (b)load_succeed();
                    Xycl data = fromJson(bytes, Xycl.class);
                    init(data);
                } else {
                    load_fail();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void init(Xycl x) {
        tv_title.setVisibility(View.VISIBLE);
        System.out.println(x.toString());
        switch (code) {
            case 1:
                if (x.data == null) {
                    setTv(tv_1, "0");
                    setTv(tv_2, "0");
                    setTv(tv_3, "0");
                    tv_title.setBackgroundResource(R.color.touming);
                    setTv(tv_time, "还没有数据，快去添加吧");
                    return;
                }
                String str = ssyCompareSzy(ssy(x.data.getSsy()), szy(x.data.getSzy()));
                tv_title.setText(str);
                if (str.equals("偏低")) {
                    tv_title.setBackgroundResource(R.drawable.pd);
                }
                if (str.equals("正常")) {
                    tv_title.setBackgroundResource(R.drawable.zc);
                }
                if (str.equals("一级")) {
                    tv_title.setBackgroundResource(R.drawable.yiji);
                }
                if (str.equals("二级")) {
                    tv_title.setBackgroundResource(R.drawable.erji);
                }
                if (str.equals("三级")) {
                    tv_title.setBackgroundResource(R.drawable.sanji);
                }
                setTv(tv_1, x.data.getSsy());
                setTv(tv_2, x.data.getSzy());
                setTv(tv_3, x.data.getXl());
                break;
            case 2:
//                progress("");
                if (!isRestart){
                    scanLeDevice(true);
                    bt_load.setVisibility(View.VISIBLE);
                    bt_up.setVisibility(View.VISIBLE);
                    bt.setVisibility(View.GONE);
                }else{//添加成功，显示历史记录
                    bt_load.setVisibility(View.GONE);
                    bt_up.setVisibility(View.GONE);
                    bt.setVisibility(View.VISIBLE);
                }
                if (x.data == null || x.data.getTime_name() == null || x.data.getXt() == null) {
                    setTv(tv_4, "--");
                    setTv(tv_5, "0");
                    tv_title.setBackgroundResource(R.color.touming);
                    setTv(tv_time, "还没有数据，快去添加吧");
                    return;
                } else {
                    setTv(tv_4, x.data.getTime_name());
                    setTv(tv_5, x.data.getXt());
                }
                String str2 = xt(x.data.getXt(), x.data.getTime_name());
                tv_title.setText(str2);
                if (str2.equals("偏低")) {
                    tv_title.setBackgroundResource(R.drawable.pd);
                }
                if (str2.equals("正常")) {
                    tv_title.setBackgroundResource(R.drawable.zc);
                }
                if (str2.equals("偏高")) {
                    tv_title.setBackgroundResource(R.drawable.sanji);
                }
                break;
            case 3:
                String s = "";
                if (App.getUser().getAge() == null) {
                    if (s.equals("")) {
                        s = "年龄";
                    } else {
                        s += "，年龄";
                    }
                }
                if (App.getUser().getGender() == null || App.getUser().getGender().equals("-1")) {
                    if (s.equals("")) {
                        s = "性别";
                    } else {
                        s += "，性别";
                    }
                }
                if (App.getUser().getHeight() == null) {
                    if (s.equals("")) {
                        s = "身高";
                    } else {
                        s += "，身高";
                    }
                }

                if (stringIsNull(App.getUser().getAge()) || stringIsNull(App.getUser().getHeight()) || stringIsNull(App.getUser().getGender())) {
                    show(s + "（必填）");
                }
                tv_title.setVisibility(View.GONE);
                if (x.data == null || x.data.getTz() == null) {
                    setTv(tv_num, "0");
                    tv_kg.setVisibility(View.GONE);
                    tv_title.setBackgroundResource(R.color.touming);
                } else {
                    setTv(tv_num, x.data.getTz());
                    tv_kg.setVisibility(View.VISIBLE);
                }
                break;
        }
        if (x != null && x.data != null && x.data.getCreated() != null) {
            setTv(tv_time, x.data.getCreated());
        }else{
            setTv(tv_time, "还没有数据，快去添加吧");
        }
    }

    private void show(String str) {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_ask);
            tv_ask = (TextView) dialog.findViewById(R.id.tv_content);
            dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    openActivity(GrxxActivity.class);
                }
            });
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }
        tv_ask.setText(str);
        dialog.show();
    }

    @OnClick({R.id.iv_rigth, R.id.iv_back, R.id.bt, R.id.bt_load, R.id.bt_up})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_rigth:
//                if (code == 3){
//                    startActivity(new Intent(PressureTestActivity.this,EditPressureActivity.class).putExtra("code",code));
//                }else{
//                    startActivity(new Intent(PressureTestActivity.this,RecordPressureActivity.class).putExtra("code",code));
//                }
                startActivity(new Intent(PressureTestActivity.this, RecordPressureActivity.class).putExtra("code", code));
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt://历史记录
                startActivity(new Intent(PressureTestActivity.this, HistoryActivity.class).putExtra("code", code));
                break;
            case R.id.bt_load:
//                progress("");
                if (mConnected){//已连接
                    return;
                }
                bt_load.setText("搜索中");
                scanLeDevice(true);
//                startActivity(new Intent(PressureTestActivity.this, HistoryActivity.class).putExtra("code", code));
                break;
            case R.id.bt_up://蓝牙上传
                if (mConnected) {
                    progress("上传中...");
                    SPP_SendHex(EhUtils.hexStringToBytes("26445A20063336380D"));
                } else {
                    toask("未连接血糖仪");
                }
//                Message message = Message.obtain();
//                message.what = 29;
//                mHandler.sendMessageDelayed(message, 3000);
//                startActivity(new Intent(PressureTestActivity.this, HistoryActivity.class).putExtra("code", code));
                break;
        }
    }

    private void set_ll(LinearLayout ll) {
        ll_1.setVisibility(View.GONE);
        ll_2.setVisibility(View.GONE);
        ll_3.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
    }

    boolean isRestart = false;

    @Override
    protected void onRestart() {
        getData(false);
        if (SharedPreferencesUtils.getString("code").equals("2")){
            isRestart = true;
            bt_load.setVisibility(View.GONE);
            bt_up.setVisibility(View.GONE);
            bt.setVisibility(View.VISIBLE);
            SharedPreferencesUtils.cleanString("code");
        }
        super.onRestart();
    }

    /**
     * 搜索蓝牙设备
     *
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {//10秒后扫描结束
                    hideProgress();
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    if (isOk) {
                        bt_load.setText("已连接");
                    } else {
                        bt_load.setText("未连接");
                    }
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        if (mGattUpdateReceiver != null) {
            unregisterReceiver(mGattUpdateReceiver);
        }
    }


    // Device scan callback.驱动搜索回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String srvAdvData = byte2HexStr(scanRecord);
                    if (EH_FilterUUID_128(srvAdvData)) {//如果扫描到了设备
//                        if (mLeDevices.contains(device)) {//如果该设备已经被扫描过。则不需配对
//                            return;
//                        } else {
////                            toask("扫描成功");
//                            mLeDevices.add(device);
//                        }
                        isOk = true;
                        mDeviceAddress = device.getAddress();
                        mDeviceName = device.getName();
                        comLy();
//                        return;
                    }
                }
            });
            Log.i("scan", "rssi = " + rssi);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mGattCharacteristics != null) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            //Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onDestroy() {
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        mBluetoothLeService = null;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 返回的数据
     */
    String result = "";
    /**
     * 返回的数据
     */
    String upload = "";
    private boolean bFlagHex = false;

    private void displayData(String data) {
        if (data != null) {

            if (!bFlagHex) {
                byte[] dd = data.getBytes();
                final StringBuilder stringBuilder = new StringBuilder(dd.length);
                for (byte byteChar : dd) {
                    stringBuilder.append(String.format("%02x ", byteChar));
                }
                String s = data.substring(data.length() - 1);
                result += stringBuilder.toString();
                if (s.equals("\r")) {//接收完成
//                    toask("接收完成");
                    upload = result.replace(" ", "");
                    upload = upload.substring(14, upload.length() - 16);
//                    result = "";
                    String[] value = upload.split("1e");
                    List<Xt> list = new ArrayList<Xt>();
                    if (list != null) {
                        list.clear();
                    }
                    for (int i = 0; i < value.length; i++) {
                        list.add(string2Xt(value[i]));
                    }
                    xt_ly(0, list);
//                    tv.setText(result);
                }
            } else {
                result += data;
                System.out.println(data);
//                tv.setText(result);
            }

        }

    }

    /**
     * 血糖蓝牙上传
     *
     * @param index
     * @param list
     */
    void xt_ly(final int index, final List<Xt> list) {
        progress("上传中...");
        if (list.get(index)==null){
            if (index>=list.size()){
                return;
            }else{
                xt_ly(index+1,list);
            }
        }
        AsyncHttpParams asyncHttpParams = new AsyncHttpParams();
        asyncHttpParams.put("user_id", App.getUser().getId());
        asyncHttpParams.put("xt", list.get(index).xt);
        asyncHttpParams.put("time_name", list.get(index).time_name);
        asyncHttpParams.put("created", list.get(index).time);
        AsyncHttpRequest.post(addUrl, asyncHttpParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                hideProgress();
                if (statusCode == 200) {
                    if (index < list.size() - 1) {
                        xt_ly(index + 1, list);
                    } else {
                        toask("上传成功");
                        getData(false);
                    }
                } else {
                    toask("上传失败");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                no_network(error);
            }
        });
    }

    void toask(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private BluetoothGattService returnAppservice(UUID uuid) {

        List<BluetoothGattService> services = mBluetoothLeService.getSupportedGattServices();

        for (BluetoothGattService gattService : services) {
            Log.i("jinxin", "uuid of service=" + gattService.getUuid().toString());
            if (uuid.equals(gattService.getUuid())) {

                return gattService;
            }
        }
        return null;
    }

    private void AppGetSerivesAndCharacteristic() {
        sppService = returnAppservice(UUID.fromString(ServiceUUID));
        sppCharacteristic = sppService.getCharacteristics().get(0);
        SwitchCharacteristic = sppService.getCharacteristics().get(1);
        SPP_StartRev(true);
    }

    private void SPP_SendData(String buf) {
        if (sppCharacteristic == null || sppCharacteristic.getUuid() == null) {
            Toast.makeText(this, "可能被其它设备连接", Toast.LENGTH_SHORT);
//            final Intent intent = new Intent(this, DeviceScanActivity.class);
//            startActivity(intent);
            return;
        }
        //if(sppCharacteristic.getUuid().equals(UUID.fromString(SPP_CHAN_UUID))){
        if (true) {
            ////Log.i("spp", "char1 uuid = "+sppCharacteristic.getUuid());
            byte[] p = buf.getBytes();
            sppCharacteristic.setValue(p);
            sppCharacteristic.setWriteType(sppCharacteristic.getWriteType());
            mBluetoothLeService.writeCharacteristic(sppCharacteristic);
        }

    }

    private void SPP_SendHex(byte[] buf) {
        if (sppCharacteristic == null || sppCharacteristic.getUuid() == null) {
            toask("可能被其它设备连接");
//            Toast.makeText(this, "可能被其它设备连接", Toast.LENGTH_SHORT).show();
//            final Intent intent = new Intent(this, DeviceScanActivity.class);
//            startActivity(intent);
            return;
        }
        //if(sppCharacteristic.getUuid().equals(UUID.fromString(SPP_CHAN_UUID))){
        if (true) {
            sppCharacteristic.setValue(buf);
            sppCharacteristic.setWriteType(sppCharacteristic.getWriteType());
            mBluetoothLeService.writeCharacteristic(sppCharacteristic);
//            toask("发送成功");
        }

    }

    private void SPP_StartRev(boolean f) {
        mBluetoothLeService.setCharacteristicNotification(
                sppCharacteristic, f);
    }

    /**
     * 判断是否是血糖仪
     *
     * @param srcAdvData
     * @return
     */
    public static boolean EH_FilterUUID_128(String srcAdvData) {

        if (srcAdvData.substring(10, 42).equalsIgnoreCase(EH_MC10_DEFAULT_UUID128)) {
            return true;
        }
        return false;
    }

    private static final String[] hexArr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public static String byte2HexStr(byte[] byt) {
        StringBuffer strRet = new StringBuffer();
        for (int i = 0; i < byt.length; i++) {
            strRet.append(hexArr[(byt[i] & 0xf0) / 16]);
            strRet.append(hexArr[byt[i] & 0x0f]);
        }
        return strRet.toString();
    }

    private ExpandableListView.OnChildClickListener servicesListClickListner;

    private ServiceConnection mServiceConnection;
// = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            if (!mBluetoothLeService.initialize()) {
//                //Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
//            }
//            // Automatically connects to the device upon successful start-up initialization.
//            mBluetoothLeService.connect(mDeviceAddress);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mBluetoothLeService = null;
//        }
//    };

    /**
     * 连接血糖仪
     */
    void comLy() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
                if (!mBluetoothLeService.initialize()) {
                    //Log.e(TAG, "Unable to initialize Bluetooth");
                    finish();
                }
                // Automatically connects to the device upon successful start-up initialization.
                mBluetoothLeService.connect(mDeviceAddress);

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBluetoothLeService = null;
            }
        };
//        mBluetoothLeService = ((BluetoothLeService.LocalBinder) mServiceConnection).getService();
//        if (mBluetoothLeService != null) {
//            mBluetoothLeService = new BluetoothLeService();
//        }
//        mBluetoothLeService.connect(mDeviceAddress);
        servicesListClickListner =
                new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                                int childPosition, long id) {
                        if (mGattCharacteristics != null) {
                            final BluetoothGattCharacteristic characteristic =
                                    mGattCharacteristics.get(groupPosition).get(childPosition);
                            final int charaProp = characteristic.getProperties();
                            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                                // If there is an active notification on a characteristic, clear
                                // it first so it doesn't update the data field on the user interface.
                                if (mNotifyCharacteristic != null) {
                                    mBluetoothLeService.setCharacteristicNotification(
                                            mNotifyCharacteristic, false);
                                    mNotifyCharacteristic = null;
                                }
                                mBluetoothLeService.readCharacteristic(characteristic);
                            }
                            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                mNotifyCharacteristic = characteristic;
                                mBluetoothLeService.setCharacteristicNotification(
                                        characteristic, true);
                                //Log.i("spp", "------------noti---------");
                            }
                            return true;
                        }
                        return false;
                    }
                };
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//        if ( bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE)){
//            toask("xxxxxxxx");
//        }
//        byte[] p = EhUtils.hexStringToBytes("26445A20063336380D");
//        SPP_StartRev(true);
//        Message message = Message.obtain();
//        message.what = 29;
//        mHandler.sendMessageDelayed(message, 3000);
//        SPP_SendHex(p);
    }

    static String toString(String str) {
        char[] bytes = str.toCharArray();
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
//            if ((bytes[i] == '2' && bytes[i + 1] == '0') || (bytes[i] == '3' && bytes[i + 1] == '0')) {//20,30不用解析
//                continue;
//            } else {
            if (i % 2 == 0) {
                continue;
            } else {
//                if ((bytes[i] == '2' && bytes[i + 1] == '0')) {//20不用解析
//                    continue;
//                }
                result += bytes[i];
            }
//            }
        }
        return result;
    }

    static Xt string2Xt(String str) {
        System.out.println(toString(str));
        String s = toString(str);
        Xt xt = new Xt();
        xt.time = "20" + s.substring(4, 6) + "-" + s.substring(0, 2) + "-" + s.substring(2, 4) + " " + s.substring(6, 8) + ":" + s.substring(8, 10);
//        xt.xt = (Double.valueOf(s.substring(10, 14)) / 18) + "";
//        String s2 = Double.valueOf(s.substring(10, 14)) / 18 + "";
//        double d =
        if (s.length()<16){
            return null;
        }
        double d_xt = Double.valueOf(s.substring(11, 14))/18 + 0.05;
        if (d_xt > 33.38) {//33.38是最大值
            d_xt = d_xt / 10;
        }
        DecimalFormat df = new DecimalFormat("0.0");
        xt.xt = df.format(d_xt);//保留小数点后一位
//        String[] s1 = s2.split("\\.");
//        if (s1.length > 1) {
//            xt.xt = s1[0] + "." + s1[1].substring(0, 1);
//        }
        //测量时间段
        String timeName = s.substring(14, 16);
        if (timeName.equals("10")) {
            xt.time_name = "餐后";
        } else if (timeName.equals("12")) {
            xt.time_name = "餐前";
        } else if (timeName.equals("40")) {
            xt.time_name = "无效记录";
        } else {
            xt.time_name = "餐前";
        }
        return xt;
//        System.out.println(xt);
    }

    public void onEventMainThread(MyEvent event) {
        System.out.println(event.code);
        if (event.code == 2){//血糖添加完成，显示历史记录按钮
            bt_load.setVisibility(View.GONE);
            bt_up.setVisibility(View.GONE);
            bt.setVisibility(View.VISIBLE);
        }
    }
}
