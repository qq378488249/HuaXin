package cc.chenghong.huaxin.utils;

import java.io.Serializable;

/**
 * 断点下载信息
 * @author planet
 */
public class DownloadInfo implements Serializable{
    private static final long serialVersionUID = -6939144183169094297L;
    /** 开始点 */
    public int startPos;
    /** 结束点 */
    public int endPos;
    /** 完成度 */
    public int compeleteSize;
    /** 下载url */
    public String url;
    /** 保存文件名 */
    public String fileName;
    public boolean downloadSuccess = false;
}