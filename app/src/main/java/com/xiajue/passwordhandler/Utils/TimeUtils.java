package com.xiajue.passwordhandler.Utils;

import android.os.AsyncTask;

import com.xiajue.passwordhandler.consts.Const;

import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

/**
 * xiaJue 2018/2/1创建
 */
public class TimeUtils {

    public static void getNetTime(final OnTimeGetCallback callback, final String... webSiteStr) {
        new AsyncTask<Void, Void, Calendar>() {
            @Override
            protected Calendar doInBackground(Void... params) {
                L.e("get net...>>>>>>>>>>>>>>>>>>");
                try {
                    String webSite = Const.BAIDU;
                    if (webSiteStr != null && webSiteStr.length > 0) {
                        webSite = webSiteStr[0];
                    }
                    URL url = new URL(webSite);
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long ld = uc.getDate(); //取得网站日期时间
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(ld);
                    return calendar;
//                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    final String format = formatter.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Calendar calendar) {
                callback.onFinish(calendar);
            }
        }.execute();

    }

    public static interface OnTimeGetCallback {
        /**
         * UI线程运行
         *
         * @param calendar
         */
        void onFinish(Calendar calendar);
    }

}
