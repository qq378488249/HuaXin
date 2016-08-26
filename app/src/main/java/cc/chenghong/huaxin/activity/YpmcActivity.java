package cc.chenghong.huaxin.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Ypmc;
import de.greenrobot.event.EventBus;
import ligth_blue.sortlist.CharacterParser;
import ligth_blue.sortlist.ClearEditText;
import ligth_blue.sortlist.PinyinComparator;
import ligth_blue.sortlist.SideBar;
import ligth_blue.sortlist.SortAdapter;
import ligth_blue.sortlist.SortModel;

/**
 * 药品名称 hcl 20160329
 */

public class YpmcActivity extends BaseActivity {
    @ViewInject(R.id.ll_add)
    LinearLayout ll_add;
    @ViewInject(R.id.title_layout)
    LinearLayout titleLayout;
    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.sortlist)
    ListView sortListView;
    @ViewInject(R.id.sidrbar)
    SideBar sideBar;
    @ViewInject(R.id.dialog)
    TextView dialog;

    private View mBaseView;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private Map<String, String> callRecords = new HashMap<String,String>();
    private List<Ypmc> list = new ArrayList<>();
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    List<SortModel> filterDateList;//搜索的数据集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_main_contact);
        initXutils();
        EventBus.getDefault().register(this);
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(XgxmActivity.class,12);
            }
        });
        setTitleName("药品名称");
        getData(true);
    }
    private void getData(final boolean b) {
        if (b)load();
        client().post(Api.ypmc_all, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    if (b) load_succeed();
                    Ypmc data = new Gson().fromJson(new String(bytes), Ypmc.class);
                    list.clear();
                    list.addAll(data.data);
//                    for (int i1 = 0; i1 < data.data.size(); i1++) {//添加数据
//                        if (callRecords.get(data.data.get(i1).getTitle())!=null){
//                            callRecords.put(data.data.get(i1).getTitle()+""+i,data.data.get(i1).getTitle());
//                        }else{
//                            callRecords.put(data.data.get(i1).getTitle(), data.data.get(i1).getTitle());
//                        }
//                    }
//                    System.out.println(callRecords.size());
//                    filterDateList.addAll(SourceDateList);
                    initData();
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

//    public void init() {
//        sideBar = (SideBar) this.findViewById(R.id.sidrbar);
//        dialog = (TextView) this.findViewById(R.id.dialog);
//        sortListView = (ListView) this.findViewById(R.id.sortlist);
//        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
//        title = (TextView) findViewById(R.id.title);
//    }

    // 上一次第一个可见的条目
    private int lastFirstVisibleItem = -1;

    private void initData() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                if (adapter != null && s != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position);
                    }
                }
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(YpmcActivity.this,AddDrugActivity.class);
                intent.putExtra("data",filterDateList.get(position).getName());
                setResult(RESULT_OK, intent);
                finish();
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                // Toast.makeText(getApplication(),
                // ((SortModel)adapter.getItem(position)).getName(),
                // Toast.LENGTH_SHORT).show();
//                String number = callRecords.get(((SortModel) adapter
//                        .getItem(position)).getName());
//                Toast.makeText(MainActivity.this, number, 0).show();
            }
        });

        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                // 当用户滚动的时候，判断是否需要出现挤压动画
                if (adapter != null) {
                    int nextSecPosition = adapter.getSectionForPosition(firstVisibleItem+1);
                    nextSecPosition = adapter.getPositionForSection(nextSecPosition);

                    if (firstVisibleItem != lastFirstVisibleItem) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                .getLayoutParams();
                        params.topMargin = 0;
                        titleLayout.setLayoutParams(params);
                        title.setText(SourceDateList.get(firstVisibleItem).getSortLetters());
                    }

                    if (nextSecPosition == (firstVisibleItem + 1)) {
                        View childView = view.getChildAt(0);
                        if (childView != null) {
                            int titleHeight = titleLayout.getHeight();//50
                            int bottom = childView.getBottom();
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                    .getLayoutParams();
                            if (bottom < titleHeight) {
                                float pushedDistance = bottom - titleHeight;
                                params.topMargin = (int) pushedDistance;
                                titleLayout.setLayoutParams(params);
                            } else {
                                if (params.topMargin != 0) {
                                    params.topMargin = 0;
                                    titleLayout.setLayoutParams(params);
                                }
                            }
                        }
                    }
                    lastFirstVisibleItem = firstVisibleItem;
                }
            }
        });

        new ConstactAsyncTask().execute(0);

    }

    private class ConstactAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... arg0) {
            int result = -1;
//            getData();
//            callRecords = ConstactUtil.getAllCallRecords(MainActivity.this);
            result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                List<String> constact = new ArrayList<String>();
                for (Ypmc ypmc : list) {
                    constact.add(ypmc.getTitle());
                }
//                for (Iterator<String> keys = callRecords.keySet().iterator(); keys
//                        .hasNext();) {
//                    String key = keys.next();
//                    constact.add(key);
//                }
                String[] names = new String[] {};
                names = constact.toArray(names);
                SourceDateList = filledData(names);
                filterDateList = filledData(names);
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                Collections.sort(filterDateList, pinyinComparator);
                adapter = new SortAdapter(YpmcActivity.this, SourceDateList);
                sortListView.setAdapter(adapter);

                mClearEditText = (ClearEditText) YpmcActivity.this
                        .findViewById(R.id.filter_edit);
                mClearEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View arg0, boolean arg1) {
                        mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

                    }
                });
                // 根据输入框输入值的改变来过滤搜索
                mClearEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                        filterData(s.toString());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString);
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        //这里还需要更新排头首字母
        if(filterDateList != null && !filterDateList.isEmpty()){
            title.setText(filterDateList.get(0).getSortLetters());
        }else{
            title.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(String event) {
        finish();
    }

    //    @ViewInject(R.id.et)
//    EditText et;
//    @ViewInject(R.id.bt)
//    Button bt;
//    @ViewInject(R.id.rv)
//    RefreshViewPD rv;
//    @ViewInject(R.id.lv)
//    ListView lv;
//
//    CommonAdapter<Ypmc> adapter;
//    List<Ypmc> list = new ArrayList<Ypmc>();
//
////    Page<Ypmc> page = new Page<Ypmc>(0, 10);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        contentView(R.layout.activity_ypmc);
//        initXutils();
//        setTitleName("药品名称");
//        getData(true);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (viewIsNull(et)){
//                    toask("请输入药品名称");
//                    return;
//                }
//                String s = viewGetValue(et);
//                int index = 0;
//                for (int i = 0; i < list.size(); i++) {
//                    if(list.get(i).getTitle().indexOf(s) > -1){
//                        index = i;
//                        break;
//                    }
//                }
//                if (index == 0){
//                    toask("未找到该药品信息，请手动添加");
//                }else{
//                    closeInput(et);
//                    lv.setSelection(index);
//                }
//            }
//        });
//    }
//
//    private void getData(final boolean b) {
//        load();
//        client().post(Api.ypmc_all, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                if (i == 200) {
//                    Ypmc y = fromJson(bytes, Ypmc.class);
//                    if (b) load_succeed();
//                    if (adapter == null) {
//                        initAdapter();
//                    }
//                    if (y.data.size() == 0) {
//                        toask("暂无数据");
//                        return;
//                    }
//                    list.addAll(y.data);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    load_fail();
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                load_fail();
//            }
//        });
//    }
//
//    void initAdapter() {
////        rv.openPullUp();
////        rv.openPullDown();
////        rv.setListViewScrollListener(lv);
////        rv.addOnSnapListener(new RefreshViewPD.OnSnapListener() {
////            @Override
////            public void onSnapToTop(int distance) {
////                page.firstPage();
////                getData(true);
////            }
////
////            @Override
////            public void onSnapToBottom(int distance) {
////                page.nextPage();
////                getData(false);
////            }
////        });
//        adapter = new CommonAdapter<Ypmc>(this, list, R.layout.lv_item_ypmc) {
//            @Override
//            public void convert(ViewHolder helper, Ypmc item, int position) {
//                helper.setText(R.id.tv, item.getTitle());
//            }
//        };
//
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(YpmcActivity.this,AddDrugActivity.class);
//                intent.putExtra("data",list.get(i));
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
//    }


}
