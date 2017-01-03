package com.tudou.calendarpager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.tudou.calendarpager.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends ActionBarActivity {

    private final static String TAG = "MainActivity";
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick({
            R.id.btn_month_switch, R.id.btn_week_pager, R.id.btn_expand_calendar
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_week_pager:
                startActivity(new Intent(this, WeekPagerActivity.class));
                break;
            case R.id.btn_month_switch:
                startActivity(new Intent(this, MonthSwitchActivity.class));
                break;
            case R.id.btn_expand_calendar:
                startActivity(new Intent(this, ExpandCalendarActivity.class));
                break;
        }
    }
}
