package com.xiajue.passwordhandler.manager;

import android.content.Context;

import com.xiajue.passwordhandler.Utils.SPUtils;
import com.xiajue.passwordhandler.consts.Const;

/**
 * xiaJue 2018/2/3创建
 */
public class SPManager {
    public static String getPassCipherText(Context context) {
        return SPUtils.getInstance(context).getString(Const.SP_P_KEY);
    }

    public static long getCreateMillis(Context context) {
        return SPUtils.getInstance(context).getLong(Const.SP_TIME_KEY);
    }

    public static int getLength(Context context) {
        return SPUtils.getInstance(context).getInt(Const.SP_LENGTH_KEY, 10);
    }

    public static void setPassCipherText(Context context, String cipherText) {
        SPUtils.getInstance(context).put(Const.SP_P_KEY, cipherText);
    }

    public static void setCreateMillis(Context context, long millis) {
        SPUtils.getInstance(context).put(Const.SP_TIME_KEY, millis);
    }

    public static void setLength(Context context, int length) {
        SPUtils.getInstance(context).put(Const.SP_LENGTH_KEY, length);
    }

    public static void setMM(Context context, int mm) {
        SPUtils.getInstance(context).put(Const.SP_MM_KEY, mm);
    }

    public static int getMM(Context context) {
        return SPUtils.getInstance(context).getInt(Const.SP_MM_KEY, Const.MIN_MM);
    }

    public static void setCreateMM(Context context, int mm) {
        SPUtils.getInstance(context).put(Const.SP_CREATE_MM_KEY, mm);
    }

    public static int getCreateMM(Context context) {
        return SPUtils.getInstance(context).getInt(Const.SP_CREATE_MM_KEY, getMM(context));
    }
}
