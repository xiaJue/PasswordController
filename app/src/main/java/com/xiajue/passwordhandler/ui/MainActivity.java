package com.xiajue.passwordhandler.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiajue.passwordhandler.consts.Const;
import com.xiajue.passwordhandler.Utils.L;
import com.xiajue.passwordhandler.R;
import com.xiajue.passwordhandler.manager.SPManager;
import com.xiajue.passwordhandler.Utils.TimeUtils;
import com.xiajue.passwordhandler.Utils.TimeUtils.OnTimeGetCallback;

import java.lang.reflect.Field;
import java.util.Calendar;

import static com.xiajue.passwordhandler.consts.Const.isInternetCheck;
import static com.xiajue.passwordhandler.Utils.L.isDebug;
import static com.xiajue.passwordhandler.Utils.StringUtils.createRandom;
import static com.xiajue.passwordhandler.Utils.StringUtils.toAsc;
import static com.xiajue.passwordhandler.Utils.StringUtils.toPass;

public class MainActivity extends AppCompatActivity {
    private EditText mLengthEt;
    private Button mButton;
    private TextView mRemandTv;
    private boolean isReGet;
    private boolean isReSet;
    private Long lastMillis;
    private int mm;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isDebug = false;
        super.onCreate(savedInstanceState);
        getOverflowMenu();
        setContentView(R.layout.activity_main);
        bindViews();
        /**
         * initialize UI
         */
        pass = SPManager.getPassCipherText(this);
        lastMillis = SPManager.getCreateMillis(this);
        mm = SPManager.getCreateMM(this);
        int length = SPManager.getLength(this);
//        lastMillis = StringUtils.convertTimeToLong("2018-1-15");
        if (!pass.isEmpty()) {
            mLengthEt.setEnabled(false);
            mLengthEt.setText(String.valueOf(length));
            checkDay();
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    /**
     * bind views
     */
    private void bindViews() {
        mButton = (Button) findViewById(R.id.get_but);
        mLengthEt = (EditText) findViewById(R.id.length_et);
        mRemandTv = (TextView) findViewById(R.id.remand_tv);
    }
    //初次获取密码>>>>开启线程--->(一分钟内可获取)
    //再次获取密码>>>>if(isReGet)--->获取  else--->不可获取
    //到达规定时间后获取密码>>>获取>>>开启线程 isReset=true
    // 再次点击:if(isReset)(重置密码)

    /**
     * button click listener
     */
    public void getPass(View v) {
        //获取上次存储的信息
        pass = SPManager.getPassCipherText(this);
        lastMillis = SPManager.getCreateMillis(this);
        mm = SPManager.getCreateMM(this);
        // lastMillis = StringUtils.convertTimeToLong("2018-1-15");
        //上次保存的时间
        if (pass.isEmpty() || isReSet) {
            //create pass
            createPassword();
            if (isReGet) {
                mButton.setText(R.string.get_pass);
                isReSet = false;
            }
        } else {
            if (isReGet) {
                //reGet
                reget(pass);
            } else {
                if (isInternetCheck) {
                    //检查天数
                    checkIsCanGet();
                }
            }
        }
    }

    /**
     * 检查是否达到规定时间
     */
    private void checkIsCanGet() {
        TimeUtils.getNetTime(new OnTimeGetCallback() {
            @Override
            public void onFinish(Calendar calendar) {
                if (calendar != null) {
                    //计算时间差
                    long dd = (calendar.getTimeInMillis() - lastMillis) / (1000 * 3600 *
                            24);
                    L.e(">>>>>>>>>>>>>>dd=" + dd);
                    if (dd >= getDay()) {
                        //允许再次获取
                        remandResetPassword();
                        reget(pass);
                        isReSet = true;
                        mLengthEt.setEnabled(true);
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string
                                        .cant_get_pass) + getDay()
                                        + getString(R.string.cant_end),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.internet_error, Toast
                            .LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private long getDay() {
        return Const.DD * mm;
    }

    /**
     * 获取密码
     *
     * @param pass
     */
    private void reget(String pass) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context
                .CLIPBOARD_SERVICE);
        cm.setText(toPass(pass));
        Toast.makeText(this, R.string.reget_text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建密码
     */
    private void createPassword() {
        Integer length = getLength();
        if (length == null) return;
        String random = createRandom(false, length);
        Toast.makeText(this, R.string.create_pass_success, Toast.LENGTH_SHORT).show();
        //复制到剪切板
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(random);
        //保存到手机
        long l = System.currentTimeMillis();//当前时间
        SPManager.setPassCipherText(this, toAsc(random));
        SPManager.setCreateMillis(this, l);
        SPManager.setLength(this, length);
        SPManager.setCreateMM(this, mm);
        isReGet = true;//可以再次获取
        //一分钟后设置为不可获取
//        mRemandTv.setText(R.string.pass_remand_in);
        mLengthEt.setEnabled(false);
        startReGet();
    }

    private void remandResetPassword() {
        mButton.setText(R.string.get_new_pass);
        mRemandTv.setText(R.string.pass_remand);
        isReGet = true;
    }

    /**
     * 检查日期
     */
    private void checkDay() {
        //检查不可获取的剩余天数
        TimeUtils.getNetTime(new OnTimeGetCallback() {
            @Override
            public void onFinish(Calendar calendar) {
                if (calendar != null) {
                    long dd = getDay() - (calendar
                            .getTimeInMillis() - lastMillis) / (1000 * 3600 * 24);
                    if (dd > 0) {
                        mRemandTv.setText(getString(R.string.pass_ing) + dd +
                                getString(R.string.day_get));
                    } else {
                        mRemandTv.setText(R.string.can_get_pass);
                    }
                }
            }
        });
    }

    /**
     * 开启线程 N秒后设置为不可获取
     */
    private void startReGet() {
        isReGet = true;
        //new thread count time
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i < Const.SS_COUNT; i++) {
                    try {
                        Thread.sleep(Const.REGET_MILLS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(i);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                mRemandTv.setText(getString(R.string.pass_remand_in) +
                        (Const.SS_COUNT - values[0]));
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                isReGet = false;
                checkDay();
            }
        }.execute();
    }

    /**
     * 获取EditText中的数字
     *
     * @return
     */
    @Nullable
    private Integer getLength() {
        String numberStr = mLengthEt.getText().toString();
        if (numberStr.isEmpty() || numberStr.length() > 3) {
            Toast.makeText(this, R.string.length_error, Toast.LENGTH_SHORT).show();
            return null;
        }
        Integer length = Integer.valueOf(numberStr);
        if (length <= 5) {
            Toast.makeText(this, R.string.ddd, Toast.LENGTH_SHORT).show();
            return null;
        }
        return length;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 强制actionbar显示overflow菜单
    // force to show overflow menu in actionbar for android 4.4 below
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
