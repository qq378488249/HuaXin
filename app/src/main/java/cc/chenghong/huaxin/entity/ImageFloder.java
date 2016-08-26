package cc.chenghong.huaxin.entity;

public class ImageFloder
{
	/**
	 * 图片的文件夹路径
	 */
	private String dir;

	/**
	 * 第一张图片的路径
	 */
	private String firstImagePath;

	/**
	 * 文件夹的名称
	 */
	private String name;

	/**
	 * 图片的数量
	 */
	private int count;
	/**
	 * 是否被选中
	 */
	private boolean isSelect;

	public boolean isSelect() {
		return isSelect;
	}

	public void setIsSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDir()
	{
		return dir;
	}

	public void setDir(String dir)
	{
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
		this.name = this.dir.substring(lastIndexOf);
	}

	public String getFirstImagePath()
	{
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath)
	{
		this.firstImagePath = firstImagePath;
	}

	public String getName()
	{
		return name;
	}
	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	

}
