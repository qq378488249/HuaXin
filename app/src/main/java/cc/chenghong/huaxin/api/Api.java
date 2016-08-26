package cc.chenghong.huaxin.api;

/**
 * 华新健康接口类
 * Created by Administrator on 2016/3/15.
 */
public class Api {
    public static final String MAIN = "http://121.199.9.217/ligaoxin/app";
    /**
     * 登陆
     */
    public static final String user_login = MAIN + "/v1/user/login";
    /**
     * 2.【获取所有店铺】
     */
    public static final String organization_all = MAIN + "/v1/organization/all";
    /**
     * 3.【更新用户】[通用]
     */
    public static final String user_update = MAIN + "/v1/user/update";
    /*
     * 4.【获取用户个人信息】[通用]
     */
    public static final String user_get = MAIN + "/v1/user/get";
    /*
     * 5.【上传文件  单文件】	 [通用]
     */
    public static final String file_upload = MAIN + "/v1/file/upload";
    /*
     * 6.【提交反馈】【通用】
     */
    public static final String feedback_add = MAIN + "/v1/feedback/add";
    /*
     * 7.【促销信息—分页】【通用】
     */
    public static final String cxxx_page = MAIN + "/v1/cxxx/page";
    /*
     * 8.【促销信息—详情】【通用】
     */
    public static final String cxxx_get = MAIN + "/v1/cxxx/get";
    /*
     * 9.【健康贴士—分页】【通用】
     */
    public static final String jkts_page = MAIN + "/v1/jkts/page";
    /*
     * 10.【健康贴士—详情—包含评论】【通用】
     */
    public static final String jkts_get = MAIN + "/v1/jkts/get";
    /*
     * 11.【健康贴士—添加评论】【通用】
     */
    public static final String jkts_comment_add = MAIN + "/v1/jkts/comment_add";
    /*
     * 12.【健康贴士—点赞】【通用】
     */
    public static final String jkts_like_add = MAIN + "/v1/jkts/like_add";
    /*
     * 13.【健康贴士—取消赞】【通用】
     */
    public static final String jkts_like_cancel = MAIN + "/v1/jkts/like_cancel";
    /*
     * 14.【营养方案—分页】【通用】
     */
    public static final String yyfa_page = MAIN + "/v1/yyfa/page";
    /*
     * 15.【营养方案—详情】【通用】
     */
    public static final String yyfa_get = MAIN + "/v1/yyfa/get";
    /*
     * 16.【健康测量—体脂测量—最新一条记录】【通用】
     */
    public static final String tz_get_new = MAIN + "/v1/tz/get_new";
    /*
     * 17.【健康测量—体脂测量—添加】【通用】
     */
    public static final String tz_add = MAIN + "/v1/tz/add";
    /*
     * 18.【健康测量—体脂测量—本周记录】周一到现在【通用】
     */
    public static final String tz_get_week = MAIN + "/v1/tz/get_week";
    /*
     * 19.【健康测量—体脂测量—本月记录】1号到现在【通用】
     */
    public static final String tz_get_month = MAIN + "/v1/tz/get_month";
    /*
     * 20.【健康测量—体脂测量—本季度记录】本季度【通用】
     */
    public static final String tz_get_quarter = MAIN + "/v1/tz/get_quarter";
    /*
     * 21.【健康测量—血糖测量—最新一条记录】【通用】
     */
    public static final String xt_get_new = MAIN + "/v1/xt/get_new";
    /*
        * 22.【健康测量—血糖测量—添加】【通用】
         */
    public static final String xt_add = MAIN + "/v1/xt/add";
    /*
     * 23.【健康测量—血糖测量—本周记录】周一到现在【通用】
     */
    public static final String xt_get_week = MAIN + "/v1/xt/get_week";
    /*
     * 24.【健康测量—血糖测量—本月记录】1号到现在【通用】
     */
    public static final String xt_get_month = MAIN + "/v1/xt/get_month";
    /*
     * 25.【健康测量—血糖测量—本季度记录】本季度【通用】
     */
    public static final String xt_get_quarter = MAIN + "/v1/xt/get_quarter";
    /*
     * 26.【健康测量—血压测量—最新一条记录】【通用】
     */
    public static final String xy_get_new = MAIN + "/v1/xy/get_new";
    /*
     * 27.【健康测量—血压测量—添加】【通用】
     */
    public static final String xy_add = MAIN + "/v1/xy/add";
    /*
     *28.【健康测量—血压测量—本周记录】周一到现在【通用】
     */
    public static final String xy_get_week = MAIN + "/v1/xy/get_week";
    /*
     *29.【健康测量—血压测量—本月记录】1号到现在【通用】
     */
    public static final String xy_get_month = MAIN + "/v1/xy/get_month";
    /*
     *30.【健康测量—血压测量—本季度记录】本季度【通用】
     */
    public static final String xy_get_quarter = MAIN + "/v1/xy/get_quarter";
    /*
     *31.【运动建议—分页】【通用】
     */
    public static final String ydjy_page = MAIN + "/v1/ydjy/page";
    /*
     *32.【运动建议—详情】【通用】
     */
    public static final String ydjy_get = MAIN + "/v1/ydjy/get";
    /*
     *33.【健康预警—分页】【通用】（UI上写着健康建议）
     */
    public static final String jkyj_page = MAIN + "/v1/jkyj/page";
    /*
     *34.【健康预警—删除】【通用】（UI上写着健康建议）
     */
    public static final String jkyj_delete = MAIN + "/v1/jkyj/delete";
    /*
     *35.【门店医生—分页】【通用】
     */
    public static final String employee_page = MAIN + "/v1/employee/page";
    /*
     *36.【我的咨询—分页】【通用】
     */
    public static final String consultation_page = MAIN + "/v1/consultation/page";
    /*
     *37.【我的咨询—提交】【通用】
     */
    public static final String consultation_add = MAIN + "/v1/consultation/add";
    /*
     *38.【我的咨询—详情—回复】【通用】
     */
    public static final String consultation_reply = MAIN + "/v1/consultation/reply";
    /*
     *39.【我的咨询—详情】【通用】
     */
    public static final String consultation_get = MAIN + "/v1/consultation/get";
    /*
     *40a.空腹血糖分页
     */
    public static final String kfxt_page = MAIN + "/v1/kfxt/page";
    /*
     *40b.总胆固醇分页
     */
    public static final String zdgc_page = MAIN + "/v1/zdgc/page";
    /*
     *40c.甘油三值分页
     */
    public static final String gysz_page = MAIN + "/v1/gysz/page";
    /*
     *40d.高密度脂蛋白
     */
    public static final String gmdzdb_page = MAIN + "/v1/gmdzdb/page";
    /*
     *40e.糖化血红蛋白分页
     */
    public static final String thxhdb_page = MAIN + "/v1/thxhdb/page";
    /*
     *41a.添加空腹血糖
     */
    public static final String kfxt_add = MAIN + "/v1/kfxt/add";
    /*
     *41b.添加总胆固醇
     */
    public static final String zdgc_add = MAIN + "/v1/zdgc/add";
    /*
     *41c.添加甘油三值
     */
    public static final String gysz_add = MAIN + "/v1/gysz/add";
    /*
     *41d.添加高密度脂蛋白
     */
    public static final String gmdzdb_add = MAIN + "/v1/gmdzdb/add";
    /*
     *41e.添加糖化血红蛋白
     */
    public static final String thxhdb_add = MAIN + "/v1/thxhdb/add";
    /*
     *42a.批量删除空腹血糖分页
     */
    public static final String kfxt_batch_delete = MAIN + "/v1/kfxt/batch_delete";
    /*
     *42b.批量删除总胆固醇分页
     */
    public static final String zdgc_batch_delete = MAIN + "/v1/zdgc/batch_delete";
    /*
     *42c.批量删除甘油三值分页
     */
    public static final String gysz_batch_delete = MAIN + "/v1/gysz/batch_delete";
    /*
     *42d.批量删除高密度脂蛋白
     */
    public static final String gmdzdb_batch_delete = MAIN + "/v1/gmdzdb/batch_delete";
    /*
     *42e.批量删除糖化血红蛋白分页
     */
    public static final String thxhdb_batch_delete = MAIN + "/v1/thxhdb/batch_delete";
    /**
     * 43.【用户病例--分页】【通用】
     */
    public static final String bl_page = MAIN + "/v1/bl/page";
    /**
     * 44.【用户病例--添加】【通用】
     */
    public static final String bl_add = MAIN + "/v1/bl/add";
    /**
     * 45.【用户病例—批量删除】【通用】
     */
    public static final String bl_batch_delete = MAIN + "/v1/bl/batch_delete";
    /**
     * 46.【疾病史—所有疾病史和用户已选择的】【通用】
     */
    public static final String jbs_all_and_user = MAIN + "/v1/jbs/all_and_user";
    /**
     * 47.【疾病史—用户自己的记录--添加】【通用】
     */
    public static final String jbs_add = MAIN + "/v1/jbs/add";
    /**
     * 48.【疾病史—用户取消自己的记录】【通用】
     */
    public static final String jbs_cancel = MAIN + "/v1/jbs/cancel";
    /**
     * 49.健康预警—批量删除【通用】
     */
    public static final String jkyj_batch_delete = MAIN + "/v1/jkyj/batch_delete";
    /**
     * 50.【服药史—所有疾病史和用户已选择的】【通用】
     * /v1/fys/all_and_user
     */
    public static final String fys_all_and_user = MAIN + "/v1/fys/all_and_user";
    /**
     * 51.【服药史—用户自己的记录--添加】【通用】
     * /v1/fys/add
     */
    public static final String fys_add = MAIN + "/v1/fys/add";
    /**
     * 52.服药史—用户取消自己的记录】【通用】
     * /v1/fys/cancel
     */
    public static final String fys_calcel = MAIN + "/v1/fys/calcel";
    /**
     * 53.【体检信息—页面上的那 7个 数据】【通用】
     */
    public static final String tjxx_all_info = MAIN + "/v1/tjxx/all_info";
    /**
     * 54.【药品名称—所有】【通用】
     * /v1/ypmc/all
     */
    public static final String ypmc_all = MAIN + "/v1/ypmc/all";
    /**
     * 55.【用药信息--分页—含服药提醒】【通用】
     * /v1/yyxx/page
     */
    public static final String yyxx_page = MAIN + "/v1/yyxx/page";
    /**
     * 56.【用药信息—添加】【通用】
     * /v1/yyxx/add
     */
    public static final String yyxx_add = MAIN + "/v1/yyxx/add";
    /**
     * 57.【用药信息—更新】【通用】
     * /v1/yyxx/update
     */
    public static final String yyxx_updata = MAIN + "/v1/yyxx/update";
    /**
     * 58.【用药信息—删除】【通用】
     * /v1/yyxx/delete
     */
    public static final String yyxx_delete = MAIN + "/v1/yyxx/delete";
    /**
     * 59.【用药信息—批量删除】【通用】
     * /v1/yyxx/batch_delete
     */
    public static final String yyxx_batch_delete = MAIN + "/v1/yyxx/batch_delete";
    /**
     * 60.【用药信息—药物过敏史—所有和用户选择的】【通用】
     * /v1/ywgms/all_and_user
     */
    public static final String ywgms_all_and_user = MAIN + "/v1/ywgms/all_and_user";
    /**
     * 61.用药信息—药物过敏史—更新用户选择的】【通用】
     * /v1/ywgms/update_and_insert
     */
    public static final String ywgms_update_and_insert = MAIN + "/v1/ywgms/update_and_insert";
    /**
     * 62.【用药信息—用药禁忌症—所有和用户选择的】【通用】
     * /v1/yyjjz/all_and_user
     */
    public static final String yyjjz_all_and_user = MAIN + "/v1/yyjjz/all_and_user";
    /**
     * 63.【用药信息—用药禁忌症—更新用户选择的】【通用】
     * /v1/ywgms/update_and_insert
     */
    public static final String yyjjz_update_and_insert = MAIN + "/v1/yyjjz/update_and_insert";
    /**
     * 64.用药信息—用药过敏史—添加自定义药物】【通用】
     * /v1/ywgms/custom_insert
     */
    public static final String ywgms_custom_insert = MAIN + "/v1/ywgms/custom_insert";
    /**
     * 65.【检查版本更新】【通用】
     * /v1/version/release
     */
    public static final String ersion_elease = MAIN + "/v1/ersion/release";
    /**
     * 66.【异常记录】【通用】
     * /v1/device_exception/add
     */
    public static final String device_exception_add = MAIN + "/v1/device_exception/add";
    /**
     * 67.【手机型号采集】【通用】
     * /v1/device_info/add
     */
    public static final String device_info_add = MAIN + "/v1/device_info/add";
    /**
     * 68.健康信息—疾病史—更新用户选择的】【通用】
     * /v1/jbs/update_and_insert
     */
    public static final String jbs_update_and_insert = MAIN + "/v1/jbs/update_and_insert";
    /**
     * 69.健康信息—服药史—更新用户选择的】【通用】
     * /v1/fys/update_and_insert
     */
    public static final String fys_update_and_insert = MAIN + "/v1/fys/update_and_insert";
    /**
     * 70.【运动建议—添加评论】【通用】
     * /v1/ydjy/comment_add
     */
    public static final String ydjy_comment_add = MAIN + "/v1/ydjy/comment_add";
    /**
     * 71.【运动建议—点赞】【通用】
     * /v1/ydjy/like_add
     */
    public static final String ydjy_like_add = MAIN + "/v1/ydjy/like_add";
    /**
     * 72.【运动建议—取消赞】【通用】
     * /v1/ydjy/like_cancel
     */
    public static final String ydjy_like_cancel = MAIN + "/v1/ydjy/like_cancel";
    /**
     * 73.营养方案—添加评论】【通用】
     * /v1/yyfa/comment_add
     */
    public static final String yyfa_comment_add = MAIN + "/v1/yyfa/comment_add";
    /**
     * 74.【营养方案—点赞】【通用】
     * /v1/yyfa/like_add
     */
    public static final String yyfa_like_add = MAIN + "/v1/yyfa/like_add";
    /**
     * 75.【营养方案—取消赞】【通用】
     * /v1/yyfa/like_cancel
     */
    public static final String yyfa_like_cancel = MAIN + "/v1/yyfa/like_cancel";
    /**
     * 76.健康建议6条记录的信息】【通用】
     * /v1/info/jkjy_count
     */
    public static final String info_jkjy_count = MAIN + "/v1/info/jkjy_count";
    /**
     * 77.【设备—我的绑定设备】【通用】
     * /v1/equipment/page
     */
    public static final String equipment_page = MAIN + "/v1/equipment/page";
    /**
     * 78.【设备—绑定设备】【通用】
     * /v1/equipment/add
     */
    public static final String equipment_add = MAIN + "/v1/equipment/add";
    /**
     * 79.【设备—删除绑定的设备】【通用】
     * /v1/equipment/delete
     */
    public static final String equipment_delete = MAIN + "/v1/equipment/delete";
    /**
     * 80.【上传文件  多文件】	 [通用]  支持单传
     * /v1/file/uploads
     */
    public static final String file_uploads = MAIN + "/v1/file/uploads";
}
