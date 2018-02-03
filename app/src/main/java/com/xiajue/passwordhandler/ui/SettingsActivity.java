package com.xiajue.passwordhandler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;

import com.xiajue.passwordhandler.R;
import com.xiajue.passwordhandler.manager.SPManager;

/**
 * xiaJue 2018/2/3创建
 */
public class SettingsActivity extends AppCompatActivity {
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        bindViews();
        //initial UI
        int id = getResources().getIdentifier("s_r_" + SPManager.getMM(this), "id",
                getPackageName());
        mRadioGroup.check(id);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void bindViews() {
        mRadioGroup = (RadioGroup) findViewById(R.id.settings_radioRoot);
    }

    public void clickSave(View v) {
        int mm = getCheckRadioMM();
        SPManager.setMM(this, mm);
    }

    private int getCheckRadioMM() {
        int mm = 1;
        int id = mRadioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.s_r_1:
                mm = 1;
                break;
            case R.id.s_r_2:
                mm = 2;
                break;
            case R.id.s_r_3:
                mm = 3;
                break;
            case R.id.s_r_5:
                mm = 5;
                break;
            case R.id.s_r_6:
                mm = 6;
                break;
        }
        return mm;
    }
}
