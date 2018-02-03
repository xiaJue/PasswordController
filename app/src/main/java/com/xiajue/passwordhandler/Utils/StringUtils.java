package com.xiajue.passwordhandler.Utils;

import android.util.Log;

import com.xiajue.passwordhandler.consts.Const;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * xiaJue 2018/1/31创建
 */
public class StringUtils {
    /**
     * 创建指定数量的随机字符串
     *
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    public static String toPass(String random) {
        //可逆向的简易解密
        StringBuilder sb = new StringBuilder();
        String[] split = random.split(Const.FG_KEY);
        for (int i = 0; i < split.length; i++) {
            Log.e("toPass: ", split[i]);
            sb.append((char) Integer.parseInt(split[i]));
        }
        return sb.toString();
    }


    public static String toAsc(String random) {
        //可逆向的简易加密
        StringBuilder sb = new StringBuilder();
        char[] chars = random.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            sb.append((int) aChar);
            sb.append(Const.FG_KEY);
        }
        return sb.toString();
    }

    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
