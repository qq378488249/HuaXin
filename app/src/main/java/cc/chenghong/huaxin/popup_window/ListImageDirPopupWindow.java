package cc.chenghong.huaxin.popup_window;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import cc.chenghong.huaxin.activity.R;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.entity.ImageFloder;


public class ListImageDirPopupWindow extends BasePopupWindowForListView<cc.chenghong.huaxin.entity.ImageFloder>
{
	private ListView mListDir;

	public ListImageDirPopupWindow(int width, int height,
			List<ImageFloder> datas, View convertView)
	{
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews()
	{
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
				R.layout.list_dir_item)
		{
			@Override
			public void convert(ViewHolder helper, ImageFloder item, int position)
			{
				helper.setText(R.id.id_dir_item_name, item.getName().substring(1));
				helper.setImageByUrl(R.id.id_dir_item_image,
						item.getFirstImagePath());
				helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
				ImageView ivSelect = helper.getView(R.id.iv_select);
				if (item != null && item.isSelect()) {
					ivSelect.setVisibility(View.VISIBLE);
				} else {
					ivSelect.setVisibility(View.GONE);
				}
			}
		});
	}

	public interface OnImageDirSelected
	{
		void selected(ImageFloder floder, int position);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
	{
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents()
	{
		mListDir.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				if (mImageDirSelected != null)
				{
					mImageDirSelected.selected(mDatas.get(position),position);
				}
			}
		});
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params)
	{
		// TODO Auto-generated method stub
	}

}
