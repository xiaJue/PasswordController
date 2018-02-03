package com.xiajue.passwordhandler.consts;

/**
 * xiaJue 2018/1/31创建
 */
public class Const {

    public static String FG_KEY = "#&#";//如果分隔符带有.或者/等特殊字符需要转意.原因是正则表达式的关键字
    public static String SP_P_KEY = "pass_key";
    public static String SP_TIME_KEY = "time_key";
    public static String SP_LENGTH_KEY = "length_key";
    public static String SP_CREATE_MM_KEY = "create_mm_key";
    public static int SS_COUNT = 60;//刷新时间长度
    public static long REGET_MILLS = 1000;//刷新频率
    public static String BAIDU = "http://www.baidu.com";
    public static String BJ_TIME = "https://www.btime.com/";
    public static final int MIN_MM = 1;//默认的月数
    public static final int DD = 31;//一个月的天数
    public static final boolean isInternetCheck = true;//是否链接网络检查时间
    public static String SP_MM_KEY = "mm_key";
}
