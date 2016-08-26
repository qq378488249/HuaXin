package cc.chenghong.huaxin.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import cc.chenghong.huaxin.entity.JktsContent;

/**
 * Created by hcl on 2016/6/17.
 */
public class SortComparator implements Comparator {
    public int compare(Object arg0, Object arg1) {
        int result = 0;
        String str0 = ((JktsContent) arg0).getCreated();
        String str1 = ((JktsContent) arg1).getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d0 ;
        Date d1 ;
        try {
            d0 = sdf.parse(str0);
            d1 = sdf.parse(str1);
            if (d0.getTime() < d1.getTime()){
                result = 1;
            }else{
                result = -1;
            }
//            result = (int) (d0.getTime() - d1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        if (d0>d1){
//            return
//        }
//        System.out.println(d0 + "" + d1);
//        int flag  = DataUtils.data1_compare_data2(user0.getCreated(), user1.getCreated(), "");
//        int flag = user0.getBirthday().compareTo(user1.getBirthday());
        return result;
    }

}
