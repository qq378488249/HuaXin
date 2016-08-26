package cc.chenghong.huaxin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hcl on 2016/5/26.
 */
public class BitmapUtils {
    /**
     * 缓存文件夹名称
     */
    private static final String cachePath = "/huaxin/";
    /**
     * 压缩后的图片质量（100最高，0最低）
     */
    private static final int imageQuality = 50;
    /**
     * 保存压缩图片到sd卡，若压缩失败，则返回原始路径
     * @param initStr 原始路径
     * @return
     */
    public static String savaImage2SD(String initStr) {
        Bitmap bitmap = getimage(initStr);
        String dirPath = Environment.getExternalStorageDirectory().getPath() + cachePath;
        String imageName = System.currentTimeMillis() + ".jpeg";
        String result = "";
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdir();
        }
        File imageFile = new File(file, imageName);
        FileOutputStream fos = null;
        try {
            imageFile.createNewFile();
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,imageQuality , fos);
            result = dirPath + imageName;
            fos.flush();
        } catch (Exception e) {
            result = initStr;
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, imageQuality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = imageQuality;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
